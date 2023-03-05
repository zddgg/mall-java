package com.zddgg.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.PropertyGroupCreateVo;
import com.zddgg.mall.product.bean.PropertyGroupDetailReqVo;
import com.zddgg.mall.product.bean.PropertyGroupDetailRespVo;
import com.zddgg.mall.product.bean.PropertyGroupListReqVo;
import com.zddgg.mall.product.entity.PropertyGroup;
import com.zddgg.mall.product.entity.PropertyGroupUnit;
import com.zddgg.mall.product.entity.PropertyUnitKey;
import com.zddgg.mall.product.entity.PropertyUnitValue;
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

    private final PropertyGroupStoreService propertyGroupStoreService;

    private final PropertyUnitKeyService propertyUnitKeyService;
    private final PropertyStoreValueService propertyStoreValueService;

    private final PropertyGroupBizService propertyGroupBizService;

    public PropertyGroupController(PropertyGroupService propertyGroupService,
                                   PropertyGroupStoreService propertyGroupStoreService,
                                   PropertyUnitKeyService propertyUnitKeyService,
                                   PropertyStoreValueService propertyStoreValueService,
                                   PropertyGroupBizService propertyGroupBizService) {
        this.propertyGroupService = propertyGroupService;
        this.propertyGroupStoreService = propertyGroupStoreService;
        this.propertyUnitKeyService = propertyUnitKeyService;
        this.propertyStoreValueService = propertyStoreValueService;
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
        List<PropertyGroupUnit> groupStores = propertyGroupStoreService.list(
                new LambdaQueryWrapper<PropertyGroupUnit>()
                        .eq(PropertyGroupUnit::getPropertyGroupId, one.getPropertyGroupId()));
        List<PropertyUnitKey> propertyUnitKeys = new ArrayList<>();
        if (!CollectionUtils.isEmpty(groupStores)) {

            // 关联的属性key
            propertyUnitKeys = propertyUnitKeyService.list(
                    new LambdaQueryWrapper<PropertyUnitKey>()
                            .in(PropertyUnitKey::getUnitKeyId,
                                    groupStores.stream().map(PropertyGroupUnit::getUnitKeyId)
                                            .collect(Collectors.toList())));
            Set<String> nos = new HashSet<>();
            for (PropertyUnitKey storeKey : propertyUnitKeys) {
                nos.add(storeKey.getUnitKeyId());
            }

            if (!CollectionUtils.isEmpty(nos)) {
                // 关联的属性value
                List<PropertyUnitValue> storeValues = propertyStoreValueService.list(
                        new LambdaQueryWrapper<PropertyUnitValue>().in(PropertyUnitValue::getUnitKeyId, nos));
                Map<String, List<PropertyUnitValue>> listMap = storeValues.stream()
                        .collect(Collectors.groupingBy(PropertyUnitValue::getUnitKeyId));
                propertyUnitKeys.forEach(propertyStoreRespVo -> {
                    propertyStoreRespVo.setPropertyUnitValues(listMap.getOrDefault(propertyStoreRespVo.getUnitKeyId(), new ArrayList<>()));
                });
            }
        }
        PropertyGroupDetailRespVo res = new PropertyGroupDetailRespVo();
        res.setPropertyGroupName(one.getPropertyGroupName());
        res.setPropertyUnitKeys(propertyUnitKeys);
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
