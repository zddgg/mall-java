package com.zddgg.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.PropertyGroupCreateVo;
import com.zddgg.mall.product.bean.PropertyGroupDetailReqVo;
import com.zddgg.mall.product.bean.PropertyGroupDetailRespVo;
import com.zddgg.mall.product.bean.PropertyGroupListReqVo;
import com.zddgg.mall.product.entity.AttrUnitKey;
import com.zddgg.mall.product.entity.AttrUnitValue;
import com.zddgg.mall.product.entity.PropertyGroup;
import com.zddgg.mall.product.entity.PropertyGroupUnit;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("property/group")
public class PropertyGroupController {

    private final PropertyGroupService propertyGroupService;

    private final AttrGroupUnitService attrGroupUnitService;

    private final AttrUnitKeyService attrUnitKeyService;
    private final AttrUnitValueService attrUnitValueService;

    private final PropertyGroupBizService propertyGroupBizService;

    public PropertyGroupController(PropertyGroupService propertyGroupService,
                                   AttrGroupUnitService attrGroupUnitService,
                                   AttrUnitKeyService attrUnitKeyService,
                                   AttrUnitValueService attrUnitValueService,
                                   PropertyGroupBizService propertyGroupBizService) {
        this.propertyGroupService = propertyGroupService;
        this.attrGroupUnitService = attrGroupUnitService;
        this.attrUnitKeyService = attrUnitKeyService;
        this.attrUnitValueService = attrUnitValueService;
        this.propertyGroupBizService = propertyGroupBizService;
    }

    @PostMapping("list")
    public Result<Page<PropertyGroup>> list(@RequestBody PropertyGroupListReqVo reqVo) {
        LambdaQueryWrapper<PropertyGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNoneBlank(reqVo.getPropertyGroupName()), PropertyGroup::getPropertyGroupName, reqVo.getPropertyGroupName());
        Page<PropertyGroup> page = new Page<>(reqVo.getCurrent(), reqVo.getPageSize());
        propertyGroupService.page(page, queryWrapper);
        List<String> groupIds = page.getRecords()
                .stream()
                .map(PropertyGroup::getPropertyGroupId)
                .collect(Collectors.toList());
        List<PropertyGroup> propertyGroups = propertyGroupBizService.getListAndRelatedByGroupIds(groupIds);
        page.setRecords(propertyGroups);
        return Result.success(page);
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody @Validated PropertyGroupCreateVo vo) {
        propertyGroupBizService.saveKeyAndValue(vo);
        return Result.success();
    }

    @PostMapping("detail")
    public Result<PropertyGroupDetailRespVo> detail(@RequestBody @Validated PropertyGroupDetailReqVo req) {

        // 查询属性组信息
        LambdaQueryWrapper<PropertyGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PropertyGroup::getPropertyGroupId, req.getPropertyGroupId());
        PropertyGroup one = propertyGroupService.getOne(queryWrapper);
        if (Objects.isNull(one) || StringUtils.isBlank(one.getPropertyGroupId())) {
            throw new BizException("属性库查询失败！");
        }

        // 查询属性组关联的属性列表
        List<PropertyGroupUnit> groupStores = attrGroupUnitService.list(
                new LambdaQueryWrapper<PropertyGroupUnit>()
                        .eq(PropertyGroupUnit::getPropertyGroupId, one.getPropertyGroupId()));
        List<AttrUnitKey> AttrUnitKeys = new ArrayList<>();
        if (!CollectionUtils.isEmpty(groupStores)) {

            // 关联的属性key
            AttrUnitKeys = attrUnitKeyService.list(
                    new LambdaQueryWrapper<AttrUnitKey>()
                            .in(AttrUnitKey::getAttrId,
                                    groupStores.stream().map(PropertyGroupUnit::getUnitKeyId)
                                            .collect(Collectors.toList())));
            Set<String> nos = new HashSet<>();
            for (AttrUnitKey storeKey : AttrUnitKeys) {
                nos.add(storeKey.getAttrId());
            }

            if (!CollectionUtils.isEmpty(nos)) {
                // 关联的属性value
                List<AttrUnitValue> storeValues = attrUnitValueService.list(
                        new LambdaQueryWrapper<AttrUnitValue>().in(AttrUnitValue::getAttrId, nos));
                Map<String, List<AttrUnitValue>> listMap = storeValues.stream()
                        .collect(Collectors.groupingBy(AttrUnitValue::getAttrId));
                AttrUnitKeys.forEach(propertyStoreRespVo -> {
                    propertyStoreRespVo.setAttrUnitValues(listMap.getOrDefault(propertyStoreRespVo.getAttrId(), new ArrayList<>()));
                });
            }
        }
        PropertyGroupDetailRespVo res = new PropertyGroupDetailRespVo();
        res.setPropertyGroupId(one.getPropertyGroupId());
        res.setPropertyGroupName(one.getPropertyGroupName());
        res.setAttrUnitKeys(AttrUnitKeys);
        return Result.success(res);
    }

    @PostMapping("edit")
    public Result<Object> edit(@RequestBody @Validated PropertyGroupCreateVo vo) {
        propertyGroupBizService.edit(vo);
        return Result.success();
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody @Validated PropertyGroupDetailReqVo reqVo) {
        propertyGroupBizService.delete(reqVo);
        return Result.success();
    }
}
