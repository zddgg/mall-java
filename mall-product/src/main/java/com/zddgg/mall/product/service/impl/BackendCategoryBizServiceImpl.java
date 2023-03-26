package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.common.response.PaginationRes;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.BackendCategoryCreateReqVo;
import com.zddgg.mall.product.bean.BackendCategoryDetail;
import com.zddgg.mall.product.bean.BackendCategoryListVo;
import com.zddgg.mall.product.bean.BackendCategoryNode;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.*;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.*;
import com.zddgg.mall.product.utils.MemoryPagination;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BackendCategoryBizServiceImpl implements BackendCategoryBizService {

    private final BackendCategoryService backendCategoryService;

    private final CategoryPropertyGroupService categoryPropertyGroupService;

    private final CategoryPropertyUnitService categoryPropertyUnitService;

    private final CategoryPropertySaleService categoryPropertySaleService;

    private final PropertyGroupBizService propertyGroupBizService;

    private final PropertyUnitBizService propertyUnitBizService;

    private final PropertySaleBizService propertySaleBizService;

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
        List<String> propertyGroupIdList = reqVo.getPropertyGroupIds();
        if (!CollectionUtils.isEmpty(propertyGroupIdList)) {
            List<CategoryPropertyGroup> saveList = propertyGroupIdList.stream().map((propertyGroupId) -> {
                CategoryPropertyGroup categoryPropertyGroup = new CategoryPropertyGroup();
                categoryPropertyGroup.setCategoryId(newBackendCategory.getCategoryId());
                categoryPropertyGroup.setPropertyGroupId(propertyGroupId);
                categoryPropertyGroup.setOrderNo(0);
                categoryPropertyGroup.setStatus(StatusEnum.DELETED.code);
                return categoryPropertyGroup;
            }).collect(Collectors.toList());
            categoryPropertyGroupService.saveBatch(saveList);
        }

        // 保存类目属性库信息
        List<String> propertyUnitIds = reqVo.getPropertyUnitIds();
        if (!CollectionUtils.isEmpty(propertyUnitIds)) {
            List<CategoryPropertyUnit> saveList = propertyUnitIds.stream().map((propertyUnitId) -> {
                CategoryPropertyUnit categoryPropertyUnit = new CategoryPropertyUnit();
                categoryPropertyUnit.setCategoryId(newBackendCategory.getCategoryId());
                categoryPropertyUnit.setPropertyUnitId(propertyUnitId);
                categoryPropertyUnit.setOrderNo(0);
                categoryPropertyUnit.setStatus(StatusEnum.DISABLED.code);
                return categoryPropertyUnit;
            }).collect(Collectors.toList());
            categoryPropertyUnitService.saveBatch(saveList);
        }

        // 保存销售属性信息
        List<String> propertySaleIds = reqVo.getPropertySaleIds();
        if (!CollectionUtils.isEmpty(propertySaleIds)) {
            List<CategoryPropertySale> saveList = propertySaleIds.stream().map((propertySaleKeyId) -> {
                CategoryPropertySale categoryPropertySale = new CategoryPropertySale();
                categoryPropertySale.setCategoryId(newBackendCategory.getCategoryId());
                categoryPropertySale.setPropertySaleId(propertySaleKeyId);
                categoryPropertySale.setOrderNo(0);
                categoryPropertySale.setStatus(StatusEnum.DISABLED.code);
                return categoryPropertySale;
            }).collect(Collectors.toList());
            categoryPropertySaleService.saveBatch(saveList);
        }
    }

    @Override
    public BackendCategoryDetail detail(String categoryId) {
        // 获取当前类目信息
        BackendCategoryDetail detail = categoryDetail(categoryId);
        if (Objects.isNull(detail)) {
            throw new BizException("类目信息不存在！");
        }
        return detail;
    }

    @Override
    public List<BackendCategoryDetail> parentDetail(String categoryId) {
        if (StringUtils.isBlank(categoryId) || StringUtils.equals(categoryId, "0")) {
            return new ArrayList<>();
        }
        List<BackendCategoryNode> nodes = getAllParentNodes(categoryId);
        return nodes.stream()
                .map(backendCategoryNode ->
                        categoryDetail(backendCategoryNode.getCategoryId()))
                .sorted(Comparator.comparingInt(backendCategoryDetail -> backendCategoryDetail != null ? backendCategoryDetail.getLevel() : 0))
                .collect(Collectors.toList());
    }

    @Override
    public List<BackendCategoryDetail> parentAndSelfDetail(String categoryId) {
        if (StringUtils.isBlank(categoryId) || StringUtils.equals(categoryId, "0")) {
            return new ArrayList<>();
        }
        List<BackendCategoryNode> nodes = getAllParentNodesAndSelf(categoryId);
        return nodes.stream()
                .map(backendCategoryNode ->
                        categoryDetail(backendCategoryNode.getCategoryId()))
                .sorted(Comparator.comparingInt(backendCategoryDetail -> backendCategoryDetail != null ? backendCategoryDetail.getLevel() : 0))
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
                        .eq(CategoryPropertyGroup::getCategoryId, current.getCategoryId()));
        categoryPropertyUnitService.remove(
                new LambdaQueryWrapper<CategoryPropertyUnit>()
                        .eq(CategoryPropertyUnit::getCategoryId, current.getCategoryId()));
        categoryPropertySaleService.remove(
                new LambdaQueryWrapper<CategoryPropertySale>()
                        .eq(CategoryPropertySale::getCategoryId, current.getCategoryId()));

        // 保存类目属性组信息
        List<String> propertyGroupIds = reqVo.getPropertyGroupIds();
        if (!CollectionUtils.isEmpty(propertyGroupIds)) {
            List<CategoryPropertyGroup> saveList = propertyGroupIds.stream().map((propertyGroupId) -> {
                CategoryPropertyGroup categoryPropertyGroup = new CategoryPropertyGroup();
                categoryPropertyGroup.setCategoryId(current.getCategoryId());
                categoryPropertyGroup.setPropertyGroupId(propertyGroupId);
                categoryPropertyGroup.setOrderNo(0);
                categoryPropertyGroup.setStatus(StatusEnum.DELETED.code);
                return categoryPropertyGroup;
            }).collect(Collectors.toList());
            categoryPropertyGroupService.saveBatch(saveList);
        }

        // 保存类目属性库信息
        List<String> propertyUnitKeys = reqVo.getPropertyUnitIds();
        if (!CollectionUtils.isEmpty(propertyUnitKeys)) {
            List<CategoryPropertyUnit> saveList = propertyUnitKeys.stream().map((propertyStoreId) -> {
                CategoryPropertyUnit categoryPropertyUnit = new CategoryPropertyUnit();
                categoryPropertyUnit.setCategoryId(current.getCategoryId());
                categoryPropertyUnit.setPropertyUnitId(propertyStoreId);
                categoryPropertyUnit.setOrderNo(0);
                categoryPropertyUnit.setStatus(StatusEnum.DISABLED.code);
                return categoryPropertyUnit;
            }).collect(Collectors.toList());
            categoryPropertyUnitService.saveBatch(saveList);
        }

        // 保存销售属性信息
        List<String> propertySaleIds = reqVo.getPropertySaleIds();
        if (!CollectionUtils.isEmpty(propertySaleIds)) {
            List<CategoryPropertySale> saveList = propertySaleIds.stream().map((propertySaleKeyId) -> {
                CategoryPropertySale categoryPropertySale = new CategoryPropertySale();
                categoryPropertySale.setCategoryId(current.getCategoryId());
                categoryPropertySale.setPropertySaleId(propertySaleKeyId);
                categoryPropertySale.setOrderNo(0);
                categoryPropertySale.setStatus(StatusEnum.DISABLED.code);
                return categoryPropertySale;
            }).collect(Collectors.toList());
            categoryPropertySaleService.saveBatch(saveList);
        }
    }

    @Override
    public Result<PaginationRes<BackendCategoryNode>> pageList(BackendCategoryListVo vo) {
        List<BackendCategoryNode> list = getSubNodeAndSelf(vo.getCategoryId());
        list.forEach(backendCategoryNode -> backendCategoryNode.setChildren(null));
        list = list.stream().sorted(Comparator.comparing(BackendCategoryNode::getId)).collect(Collectors.toList());
        PaginationRes<BackendCategoryNode> page = MemoryPagination.page(list, vo.getCurrent(), vo.getPageSize());
        page.getRecords().forEach(backendCategoryNode -> {
            List<AttrGroup> attrGroupList = getPropertyGroupList(backendCategoryNode.getCategoryId());
            List<AttrUnitKey> propertyStoreList = getPropertyUnitList(backendCategoryNode.getCategoryId());
            List<AttrSaleKey> propertySaleList = getPropertySaleList(backendCategoryNode.getCategoryId());
            backendCategoryNode.setGroupCount(attrGroupList.size());
            backendCategoryNode.setStoreCount(propertyStoreList.size());
            backendCategoryNode.setSaleCount(propertySaleList.size());
        });
        return Result.success(page);
    }

    private BackendCategoryDetail categoryDetail(String backendCategoryNo) {
        // 类目信息查询
        BackendCategory backendCategory = backendCategoryService.getOne(
                new LambdaQueryWrapper<BackendCategory>()
                        .eq(BackendCategory::getCategoryId, backendCategoryNo));

        if (Objects.isNull(backendCategory)) {
            return null;
        }

        // 属性组信息查询
        List<AttrGroup> attrGroupList = getPropertyGroupList(backendCategoryNo);

        // 属性库信息查询
        List<AttrUnitKey> AttrUnitKeys = getPropertyUnitList(backendCategoryNo);

        // 销售属性信息
        List<AttrSaleKey> attrSaleKeys = getPropertySaleList(backendCategoryNo);

        BackendCategoryDetail respVo = new BackendCategoryDetail();
        respVo.setCategoryId(backendCategory.getCategoryId());
        respVo.setCategoryName(backendCategory.getCategoryName());
        respVo.setParentId(backendCategory.getParentId());
        respVo.setLevel(backendCategory.getLevel());
        respVo.setRelatedProperty(!CollectionUtils.isEmpty(attrGroupList) || !CollectionUtils.isEmpty(AttrUnitKeys));
        respVo.setAttrGroups(attrGroupList);
        respVo.setAttrUnitKeys(AttrUnitKeys);
        respVo.setAttrSaleKeys(attrSaleKeys);
        return respVo;
    }

    private List<AttrGroup> getPropertyGroupList(String backendCategoryId) {
        if (StringUtils.isBlank(backendCategoryId)) {
            return new ArrayList<>();
        }
        // 属性组信息查询
        List<String> groupNos = categoryPropertyGroupService.list(
                        new LambdaQueryWrapper<CategoryPropertyGroup>()
                                .eq(CategoryPropertyGroup::getCategoryId, backendCategoryId))
                .stream()
                .map(CategoryPropertyGroup::getPropertyGroupId)
                .collect(Collectors.toList());
        return propertyGroupBizService.getListAndRelatedByGroupIds(groupNos);
    }

    private List<AttrUnitKey> getPropertyUnitList(String backendCategoryId) {
        if (StringUtils.isBlank(backendCategoryId)) {
            return new ArrayList<>();
        }
        // 属性库信息查询
        List<String> propertyIds = categoryPropertyUnitService.list(
                        new LambdaQueryWrapper<CategoryPropertyUnit>()
                                .eq(CategoryPropertyUnit::getCategoryId, backendCategoryId))
                .stream()
                .map(CategoryPropertyUnit::getPropertyUnitId)
                .collect(Collectors.toList());
        return propertyUnitBizService.getListAndRelatedByPropertyIds(propertyIds);
    }

    private List<AttrSaleKey> getPropertySaleList(String backendCategoryId) {
        if (StringUtils.isBlank(backendCategoryId)) {
            return new ArrayList<>();
        }
        // 属性库信息查询
        List<String> propertyIds = categoryPropertySaleService.list(
                        new LambdaQueryWrapper<CategoryPropertySale>()
                                .eq(CategoryPropertySale::getCategoryId, backendCategoryId))
                .stream()
                .map(CategoryPropertySale::getPropertySaleId)
                .collect(Collectors.toList());
        return propertySaleBizService.getListAndRelatedByPropertyIds(propertyIds);
    }

    private BackendCategory categoryParentCheck(String parentCategoryId) {
        if (StringUtils.isNotBlank(parentCategoryId)) {
            return backendCategoryService.getOne(
                    new LambdaQueryWrapper<BackendCategory>()
                            .eq(BackendCategory::getCategoryId, parentCategoryId));
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
