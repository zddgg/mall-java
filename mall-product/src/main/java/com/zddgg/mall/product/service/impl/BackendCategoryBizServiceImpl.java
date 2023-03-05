package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.common.response.PaginationRes;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.BackendCategoryCreateReqVo;
import com.zddgg.mall.product.bean.BackendCategoryDetailRespVo;
import com.zddgg.mall.product.bean.BackendCategoryListVo;
import com.zddgg.mall.product.bean.BackendCategoryNode;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.*;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.*;
import com.zddgg.mall.product.utils.MemoryPagination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BackendCategoryBizServiceImpl implements BackendCategoryBizService {

    private final BackendCategoryService backendCategoryService;

    private final CategoryPropertyGroupService categoryPropertyGroupService;

    private final CategoryPropertyStoreService categoryPropertyStoreService;

    private final PropertyGroupBizService propertyGroupBizService;

    private final PropertyUnitBizService propertyUnitBizService;

    public BackendCategoryBizServiceImpl(BackendCategoryService backendCategoryService,
                                         CategoryPropertyGroupService categoryPropertyGroupService,
                                         CategoryPropertyStoreService categoryPropertyStoreService,
                                         PropertyGroupBizService propertyGroupBizService,
                                         PropertyUnitBizService propertyUnitBizService) {
        this.backendCategoryService = backendCategoryService;
        this.categoryPropertyGroupService = categoryPropertyGroupService;
        this.categoryPropertyStoreService = categoryPropertyStoreService;
        this.propertyGroupBizService = propertyGroupBizService;
        this.propertyUnitBizService = propertyUnitBizService;
    }

    /**
     * 从跟类目开始生成类目树
     *
     * @param maxLevel 查询的最大层级
     * @return
     */
    @Override
    public List<BackendCategoryNode> getRootList(Integer maxLevel) {
        List<BackendCategory> backendCategories = backendCategoryService.list(
                new LambdaQueryWrapper<BackendCategory>()
                        .le(Objects.nonNull(maxLevel), BackendCategory::getLevel, maxLevel)
                        .ne(BackendCategory::getStatus, StatusEnum.DELETED.code));
        return getParentNoMap(backendCategories).get("0");
    }

    @Override
    public BackendCategoryNode getNode(String backendCategoryNo) {
        List<BackendCategory> backendCategories = backendCategoryService.list();
        return getNode(backendCategoryNo, backendCategories);
    }

    @Override
    public List<BackendCategoryNode> getSubNode(String backendCategoryNo) {
        List<BackendCategory> backendCategories = backendCategoryService.list();
        return getSubNode(backendCategoryNo, backendCategories);
    }

    @Override
    public List<BackendCategoryNode> getSubNodeAndSelf(String backendCategoryNo) {
        List<BackendCategory> backendCategories = backendCategoryService.list();
        BackendCategoryNode self = getNode(backendCategoryNo, backendCategories);
        List<BackendCategoryNode> resNodes = new ArrayList<>();
        if (Objects.nonNull(self)) {
            resNodes.add(self);
        }
        List<BackendCategoryNode> subNode = getSubNode(backendCategoryNo, backendCategories);
        resNodes.addAll(subNode);
        return resNodes;
    }

    @Override
    public List<BackendCategoryNode> getAllParentNodes(String backendCategoryNo) {
        List<BackendCategory> backendCategories = backendCategoryService.list();
        return getAllParentNodes(backendCategoryNo, backendCategories);
    }

    @Override
    public List<BackendCategoryNode> getAllParentNodesAndSelf(String backendCategoryNo) {
        List<BackendCategory> backendCategories = backendCategoryService.list();
        ArrayList<BackendCategoryNode> res = new ArrayList<>();
        BackendCategoryNode self = getNode(backendCategoryNo, backendCategories);
        res.add(self);
        List<BackendCategoryNode> allParentNodes = getAllParentNodes(backendCategoryNo, backendCategories);
        res.addAll(allParentNodes);
        return res;
    }

    @Transactional
    @Override
    public void create(BackendCategoryCreateReqVo reqVo) {

        // 上级类目校验
        BackendCategory categoryParent = categoryParentCheck(reqVo.getParentId());
        if (StringUtils.isNotBlank(reqVo.getParentId()) && Objects.isNull(categoryParent)) {
            throw new BizException("[" + reqVo.getCategoryName() + "]的上级类目不存在!");
        }

        // 类目名称校验
        if (duplicateNameCheck(reqVo.getCategoryName())) {
            throw new BizException("[" + reqVo.getCategoryName() + "]类目名称已经存在!");
        }

        // 保存后台类目信息
        BackendCategory newBackendCategory = buildNewBackendCategory(categoryParent, reqVo.getCategoryName());
        backendCategoryService.save(newBackendCategory);

        // 保存类目属性组信息
        List<PropertyGroup> propertyGroupList = reqVo.getPropertyGroupList();
        if (!CollectionUtils.isEmpty(propertyGroupList)) {
            List<CategoryPropertyGroup> saveList = propertyGroupList.stream().map((propertyGroup) -> {
                CategoryPropertyGroup categoryPropertyGroup = new CategoryPropertyGroup();
                categoryPropertyGroup.setCategoryNo(newBackendCategory.getCategoryId());
                categoryPropertyGroup.setPropertyGroupNo(propertyGroup.getPropertyGroupId());
                categoryPropertyGroup.setOrderNo(0);
                categoryPropertyGroup.setStatus(StatusEnum.DELETED.code);
                return categoryPropertyGroup;
            }).collect(Collectors.toList());
            categoryPropertyGroupService.saveBatch(saveList);
        }

        // 保存类目属性库信息
        List<PropertyUnitKey> propertyUnitKeys = reqVo.getPropertyStoreList();
        if (!CollectionUtils.isEmpty(propertyUnitKeys)) {
            List<CategoryPropertyStore> saveList = propertyUnitKeys.stream().map((propertyStoreKey) -> {
                CategoryPropertyStore categoryPropertyStore = new CategoryPropertyStore();
                categoryPropertyStore.setCategoryNo(newBackendCategory.getCategoryId());
                categoryPropertyStore.setPropertyStoreNo(propertyStoreKey.getUnitKeyId());
                categoryPropertyStore.setOrderNo(0);
                categoryPropertyStore.setStatus(StatusEnum.DISABLED.code);
                return categoryPropertyStore;
            }).collect(Collectors.toList());
            categoryPropertyStoreService.saveBatch(saveList);
        }
    }

    @Override
    public BackendCategoryDetailRespVo detail(String backendCategoryNo) {
        // 获取当前类目信息
        BackendCategoryDetailRespVo respVo = categoryDetail(backendCategoryNo);
        if (Objects.isNull(respVo)) {
            throw new BizException("类目信息不存在！");
        }

        List<BackendCategoryNode> allParentNodes = getAllParentNodes(backendCategoryNo);
        List<BackendCategoryDetailRespVo> parentCategoryInfo = allParentNodes.stream()
                .map(backendCategoryNode ->
                        categoryDetail(backendCategoryNode.getCategoryId()))
                .sorted(Comparator.comparingInt(backendCategoryDetailRespVo -> backendCategoryDetailRespVo != null ? backendCategoryDetailRespVo.getLevel() : 0))
                .collect(Collectors.toList());
        respVo.setParentCategoryDetail(parentCategoryInfo);
        return respVo;
    }

    @Override
    public List<BackendCategoryDetailRespVo> allParentDetail(String parentCategoryNo) {
        if (StringUtils.isBlank(parentCategoryNo)) {
            return new ArrayList<>();
        }
        List<BackendCategoryNode> nodes = getAllParentNodesAndSelf(parentCategoryNo);
        return nodes.stream()
                .map(backendCategoryNode ->
                        categoryDetail(backendCategoryNode.getCategoryId()))
                .sorted(Comparator.comparingInt(backendCategoryDetailRespVo -> backendCategoryDetailRespVo != null ? backendCategoryDetailRespVo.getLevel() : 0))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void update(BackendCategoryCreateReqVo reqVo) {
        // 上级类目校验
        BackendCategory parentCategory = categoryParentCheck(reqVo.getParentId());
        if (!(StringUtils.isBlank(reqVo.getParentId())
                || StringUtils.equals(reqVo.getParentId(), "0")) && Objects.isNull(parentCategory)) {
            throw new BizException("[" + reqVo.getCategoryName() + "]的上级类目不存在!");
        }

        // 获取当前类目
        BackendCategory current = backendCategoryService.getOne(
                new LambdaQueryWrapper<BackendCategory>()
                        .eq(BackendCategory::getCategoryId, reqVo.getCategoryId()));
        if (Objects.isNull(current)) {
            throw new BizException("[" + reqVo.getCategoryName() + "]类目不存在!");
        }

        current.setCategoryName(reqVo.getCategoryName());
        if (StringUtils.isNotBlank(reqVo.getParentId())) {
            current.setParentId(reqVo.getParentId());
        }
        int parentLevel = 0;
        if (Objects.nonNull(parentCategory)) {
            parentLevel = Optional.ofNullable(parentCategory.getLevel()).orElse(0);
        }
        current.setLevel(parentLevel + 1);

        // 更新后台类目信息
        backendCategoryService.updateById(current);

        // 删除关联属性信息
        categoryPropertyGroupService.remove(
                new LambdaQueryWrapper<CategoryPropertyGroup>()
                        .eq(CategoryPropertyGroup::getCategoryNo, current.getCategoryId()));
        categoryPropertyStoreService.remove(
                new LambdaQueryWrapper<CategoryPropertyStore>()
                        .eq(CategoryPropertyStore::getCategoryNo, current.getCategoryId()));

        // 保存类目属性组信息
        List<PropertyGroup> propertyGroupList = reqVo.getPropertyGroupList();
        if (!CollectionUtils.isEmpty(propertyGroupList)) {
            List<CategoryPropertyGroup> saveList = propertyGroupList.stream().map((propertyGroup) -> {
                CategoryPropertyGroup categoryPropertyGroup = new CategoryPropertyGroup();
                categoryPropertyGroup.setCategoryNo(current.getCategoryId());
                categoryPropertyGroup.setPropertyGroupNo(propertyGroup.getPropertyGroupId());
                categoryPropertyGroup.setOrderNo(0);
                categoryPropertyGroup.setStatus(StatusEnum.DELETED.code);
                return categoryPropertyGroup;
            }).collect(Collectors.toList());
            categoryPropertyGroupService.saveBatch(saveList);
        }

        // 保存类目属性库信息
        List<PropertyUnitKey> propertyUnitKeys = reqVo.getPropertyStoreList();
        if (!CollectionUtils.isEmpty(propertyUnitKeys)) {
            List<CategoryPropertyStore> saveList = propertyUnitKeys.stream().map((propertyStoreKey) -> {
                CategoryPropertyStore categoryPropertyStore = new CategoryPropertyStore();
                categoryPropertyStore.setCategoryNo(current.getCategoryId());
                categoryPropertyStore.setPropertyStoreNo(propertyStoreKey.getUnitKeyId());
                categoryPropertyStore.setOrderNo(0);
                categoryPropertyStore.setStatus(StatusEnum.DISABLED.code);
                return categoryPropertyStore;
            }).collect(Collectors.toList());
            categoryPropertyStoreService.saveBatch(saveList);
        }
    }

    @Override
    public Result<PaginationRes<BackendCategoryNode>> pageList(BackendCategoryListVo vo) {
        List<BackendCategoryNode> list = getSubNodeAndSelf(vo.getCategoryId());
        list.forEach(backendCategoryNode -> backendCategoryNode.setChildren(null));
        list = list.stream().sorted(Comparator.comparing(BackendCategoryNode::getId)).collect(Collectors.toList());
        PaginationRes<BackendCategoryNode> page = MemoryPagination.page(list, vo.getCurrent(), vo.getPageSize());
        page.getRecords().forEach(backendCategoryNode -> {
            List<PropertyGroup> propertyGroupList = getPropertyGroupList(backendCategoryNode.getCategoryId());
            List<PropertyUnitKey> propertyStoreList = getPropertyStoreList(backendCategoryNode.getCategoryId());
            backendCategoryNode.setGroupCount(propertyGroupList.size());
            backendCategoryNode.setStoreCount(propertyStoreList.size());
        });
        return Result.success(page);
    }

    private BackendCategoryDetailRespVo categoryDetail(String backendCategoryNo) {
        // 类目信息查询
        BackendCategory backendCategory = backendCategoryService.getOne(
                new LambdaQueryWrapper<BackendCategory>()
                        .eq(BackendCategory::getCategoryId, backendCategoryNo));

        if (Objects.isNull(backendCategory)) {
            return null;
        }

        // 属性组信息查询
        List<PropertyGroup> propertyGroupList = getPropertyGroupList(backendCategoryNo);

        // 属性库信息查询
        List<PropertyUnitKey> propertyStoreList = getPropertyStoreList(backendCategoryNo);

        BackendCategoryDetailRespVo respVo = new BackendCategoryDetailRespVo();
        respVo.setCategoryId(backendCategory.getCategoryId());
        respVo.setCategoryName(backendCategory.getCategoryName());
        respVo.setParentId(backendCategory.getParentId());
        respVo.setLevel(backendCategory.getLevel());
        respVo.setRelatedProperty(!CollectionUtils.isEmpty(propertyGroupList) || !CollectionUtils.isEmpty(propertyStoreList));
        respVo.setPropertyGroupList(propertyGroupList);
        respVo.setPropertyStoreList(propertyStoreList);
        return respVo;
    }

    private List<PropertyGroup> getPropertyGroupList(String backendCategoryNo) {
        if (StringUtils.isBlank(backendCategoryNo)) {
            return new ArrayList<>();
        }
        // 属性组信息查询
        List<String> groupNos = categoryPropertyGroupService.list(
                        new LambdaQueryWrapper<CategoryPropertyGroup>()
                                .eq(CategoryPropertyGroup::getCategoryNo, backendCategoryNo))
                .stream()
                .map(CategoryPropertyGroup::getPropertyGroupNo)
                .collect(Collectors.toList());
        return propertyGroupBizService.getListAndRelatedByGroupIds(groupNos);
    }

    private List<PropertyUnitKey> getPropertyStoreList(String backendCategoryNo) {
        if (StringUtils.isBlank(backendCategoryNo)) {
            return new ArrayList<>();
        }
        // 属性库信息查询
        List<String> propertyNos = categoryPropertyStoreService.list(
                        new LambdaQueryWrapper<CategoryPropertyStore>()
                                .eq(CategoryPropertyStore::getCategoryNo, backendCategoryNo))
                .stream()
                .map(CategoryPropertyStore::getPropertyStoreNo)
                .collect(Collectors.toList());
        return propertyUnitBizService.getListAndRelatedByPropertyNos(propertyNos);
    }

    private BackendCategory categoryParentCheck(String parentCategoryNo) {
        if (StringUtils.isNotBlank(parentCategoryNo)) {
            return backendCategoryService.getOne(
                    new LambdaQueryWrapper<BackendCategory>()
                            .eq(BackendCategory::getCategoryId, parentCategoryNo));
        }
        return null;
    }


    private Boolean duplicateNameCheck(String backendCategoryName) {
        BackendCategory nameCheck = backendCategoryService.getOne(
                new LambdaQueryWrapper<BackendCategory>()
                        .eq(BackendCategory::getCategoryName, backendCategoryName));
        return Objects.nonNull(nameCheck);
    }

    private BackendCategory buildNewBackendCategory(BackendCategory parentCategory, String backendCategoryName) {
        BackendCategory backendCategory = new BackendCategory();
        backendCategory.setCategoryId(UUID.randomUUID().toString().replace("-", ""));
        backendCategory.setCategoryName(backendCategoryName);
        backendCategory.setStatus(StatusEnum.DISABLED.code);

        int parentLevel = 0;
        if (Objects.nonNull(parentCategory)) {
            backendCategory.setParentId(parentCategory.getCategoryId());
            parentLevel = Optional.ofNullable(parentCategory.getLevel()).orElse(0);
        } else {
            backendCategory.setParentId("0");
        }
        backendCategory.setLevel(parentLevel + 1);
        return backendCategory;
    }

    public Map<String, List<BackendCategoryNode>> getParentNoMap(List<BackendCategory> categories) {
        Map<String, List<BackendCategoryNode>> parentNoMap = categories.stream()
                .map(backendCategory -> {
                    BackendCategoryNode backendCategoryNode = new BackendCategoryNode();
                    backendCategoryNode.setId(backendCategory.getId());
                    backendCategoryNode.setCreator(backendCategory.getCreator());
                    backendCategoryNode.setCreated(backendCategory.getCreated());
                    backendCategoryNode.setUpdater(backendCategory.getUpdater());
                    backendCategoryNode.setUpdated(backendCategory.getUpdated());
                    backendCategoryNode.setCategoryId(backendCategory.getCategoryId());
                    backendCategoryNode.setCategoryName(backendCategory.getCategoryName());
                    backendCategoryNode.setParentId(backendCategory.getParentId());
                    backendCategoryNode.setLevel(backendCategory.getLevel());
                    backendCategoryNode.setStatus(backendCategory.getStatus());
                    return backendCategoryNode;
                })
                .collect(Collectors.groupingBy(BackendCategoryNode::getParentId));
        parentNoMap.forEach((key, value) ->
                value.forEach(tree -> tree.setChildren(parentNoMap.get(tree.getCategoryId()))));
        return parentNoMap;
    }


    public Map<String, BackendCategoryNode> getCategoryNoMap(List<BackendCategory> categories) {
        return getParentNoMap(categories).values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(BackendCategoryNode::getCategoryId,
                        backendCategoryNode -> backendCategoryNode));
    }


    public void findAllSubNode(List<BackendCategoryNode> data, List<BackendCategoryNode> result) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        for (BackendCategoryNode item : data) {
            result.add(item);
            if (!CollectionUtils.isEmpty(item.getChildren())) {
                findAllSubNode(item.getChildren(), result);
            }
        }
    }

    public void findAllParentNode(String parentCategoryNo,
                                  Map<String, BackendCategoryNode> categoryNoMap,
                                  List<BackendCategoryNode> result) {
        BackendCategoryNode currentNode = categoryNoMap.get(parentCategoryNo);
        if (Objects.isNull(currentNode)) {
            return;
        }
        result.add(currentNode);
        findAllParentNode(currentNode.getParentId(), categoryNoMap, result);
    }

    private BackendCategoryNode getNode(String backendCategoryNo, List<BackendCategory> backendCategories) {
        Map<String, List<BackendCategoryNode>> parentNoMap = getParentNoMap(backendCategories);
        List<BackendCategoryNode> categoryTrees = parentNoMap.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return categoryTrees.stream()
                .filter(categoryTree -> StringUtils.equals(categoryTree.getCategoryId(), backendCategoryNo))
                .findAny().orElse(null);
    }

    private List<BackendCategoryNode> getSubNode(String backendCategoryNo, List<BackendCategory> backendCategories) {
        Map<String, List<BackendCategoryNode>> parentNoMap = getParentNoMap(backendCategories);

        List<BackendCategoryNode> resNodes = new ArrayList<>();
        if (StringUtils.isBlank(backendCategoryNo)) {
            resNodes = parentNoMap.values().stream().flatMap(Collection::stream)
                    .collect(Collectors.toList());
        } else {
            List<BackendCategoryNode> subTreeList = parentNoMap.get(backendCategoryNo);
            findAllSubNode(subTreeList, resNodes);
        }
        return resNodes;
    }

    public List<BackendCategoryNode> getAllParentNodes(String backendCategoryNo, List<BackendCategory> backendCategories) {
        BackendCategory currentNode = backendCategories.stream()
                .filter(backendCategory ->
                        StringUtils.equals(backendCategory.getCategoryId(), backendCategoryNo))
                .findAny().orElseThrow(() -> new BizException("后台类目查询失败，类目不存在！"));
        Map<String, BackendCategoryNode> categoryNoMap = getCategoryNoMap(backendCategories);
        List<BackendCategoryNode> parentList = new ArrayList<>();
        findAllParentNode(currentNode.getParentId(), categoryNoMap, parentList);
        return parentList;
    }
}
