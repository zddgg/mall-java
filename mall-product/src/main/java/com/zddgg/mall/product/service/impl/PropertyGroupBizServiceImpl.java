package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.product.bean.PropertyGroupCreateVo;
import com.zddgg.mall.product.bean.PropertyGroupDetailReqVo;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.PropertyGroup;
import com.zddgg.mall.product.entity.PropertyGroupUnit;
import com.zddgg.mall.product.entity.PropertyUnitKey;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.PropertyGroupBizService;
import com.zddgg.mall.product.service.PropertyGroupService;
import com.zddgg.mall.product.service.PropertyGroupStoreService;
import com.zddgg.mall.product.service.PropertyUnitBizService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PropertyGroupBizServiceImpl implements PropertyGroupBizService {

    private final PropertyGroupService propertyGroupService;

    private final PropertyGroupStoreService propertyGroupStoreService;

    private final PropertyUnitBizService propertyUnitBizService;

    public PropertyGroupBizServiceImpl(PropertyGroupService propertyGroupService,
                                       PropertyGroupStoreService propertyGroupStoreService,
                                       PropertyUnitBizService propertyUnitBizService) {
        this.propertyGroupService = propertyGroupService;
        this.propertyGroupStoreService = propertyGroupStoreService;
        this.propertyUnitBizService = propertyUnitBizService;
    }

    @Transactional
    @Override
    public void saveKeyAndValue(PropertyGroupCreateVo vo) {
        LambdaQueryWrapper<PropertyGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PropertyGroup::getPropertyGroupName, vo.getPropertyGroupName());
        PropertyGroup one = propertyGroupService.getOne(queryWrapper);
        if (Objects.nonNull(one)) {
            throw new BizException("属性组名称[" + vo.getPropertyGroupName() + "]已存在！");
        }
        PropertyGroup propertyGroup = new PropertyGroup();
        propertyGroup.setPropertyGroupId(UUID.randomUUID().toString().replace("-", ""));
        propertyGroup.setPropertyGroupName(vo.getPropertyGroupName());
        propertyGroup.setStatus(StatusEnum.DISABLED.code);
        propertyGroupService.save(propertyGroup);

        ArrayList<PropertyGroupUnit> saveList = new ArrayList<>();
        for (PropertyUnitKey propertyUnitKey : vo.getPropertyUnitKeys()) {
            PropertyGroupUnit value = new PropertyGroupUnit();
            value.setPropertyGroupId(propertyGroup.getPropertyGroupId());
            value.setUnitKeyId(propertyUnitKey.getUnitKeyId());
            value.setPropertyOrder(0);
            saveList.add(value);
        }
        propertyGroupStoreService.saveBatch(saveList);
    }

    @Transactional
    @Override
    public void edit(PropertyGroupCreateVo vo) {
        LambdaQueryWrapper<PropertyGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PropertyGroup::getPropertyGroupId, vo.getPropertyGroupId());
        PropertyGroup one = propertyGroupService.getOne(queryWrapper);
        if (Objects.isNull(one)) {
            throw new BizException("属性组不存在，请刷新列表！");
        }
        one.setPropertyGroupName(vo.getPropertyGroupName());
        propertyGroupService.updateById(one);

        propertyGroupStoreService.remove(
                new LambdaQueryWrapper<PropertyGroupUnit>()
                        .eq(PropertyGroupUnit::getPropertyGroupId, one.getPropertyGroupId()));

        ArrayList<PropertyGroupUnit> saveList = new ArrayList<>();
        for (PropertyUnitKey propertyUnitKey : vo.getPropertyUnitKeys()) {
            PropertyGroupUnit value = new PropertyGroupUnit();
            value.setPropertyGroupId(one.getPropertyGroupId());
            value.setUnitKeyId(propertyUnitKey.getUnitKeyId());
            value.setPropertyOrder(0);
            saveList.add(value);
        }
        propertyGroupStoreService.saveBatch(saveList);
    }

    @Transactional
    @Override
    public void delete(PropertyGroupDetailReqVo reqVo) {
        propertyGroupService.remove(
                new LambdaQueryWrapper<PropertyGroup>()
                        .eq(PropertyGroup::getPropertyGroupId, reqVo.getPropertyGroupId()));
        propertyGroupStoreService.remove(
                new LambdaQueryWrapper<PropertyGroupUnit>()
                        .eq(PropertyGroupUnit::getPropertyGroupId, reqVo.getPropertyGroupId()));
    }

    @Override
    public List<PropertyGroup> getListAndRelatedByGroupIds(List<String> groupIds) {
        if (CollectionUtils.isEmpty(groupIds)) {
            return new ArrayList<>();
        }
        List<PropertyGroup> propertyGroups = propertyGroupService.list(
                new LambdaQueryWrapper<PropertyGroup>()
                        .in(PropertyGroup::getPropertyGroupId, groupIds));
        List<PropertyGroupUnit> propertyGroupUnits = propertyGroupStoreService.list(
                new LambdaQueryWrapper<PropertyGroupUnit>()
                        .in(PropertyGroupUnit::getPropertyGroupId, groupIds));
        if (!CollectionUtils.isEmpty(propertyGroupUnits)) {
            List<String> storeNos = propertyGroupUnits.stream()
                    .map(PropertyGroupUnit::getUnitKeyId).collect(Collectors.toList());
            Map<String, List<String>> group2StoreMap = propertyGroupUnits.stream()
                    .collect(Collectors.groupingBy(PropertyGroupUnit::getPropertyGroupId,
                            Collectors.mapping(PropertyGroupUnit::getUnitKeyId, Collectors.toList())));
            List<PropertyUnitKey> propertyUnitKeys = propertyUnitBizService.getListAndRelatedByPropertyNos(storeNos);
            Map<String, PropertyUnitKey> storeMap = propertyUnitKeys
                    .stream()
                    .collect(Collectors.toMap(PropertyUnitKey::getUnitKeyId,
                            propertyStoreRespVo -> propertyStoreRespVo));
            propertyGroups.forEach(propertyGroup -> {
                List<PropertyUnitKey> storeKeys = group2StoreMap
                        .getOrDefault(propertyGroup.getPropertyGroupId(), new ArrayList<>())
                        .stream().map(storeMap::get).collect(Collectors.toList());
                propertyGroup.setPropertyUnitKeys(storeKeys);
            });

        }
        return propertyGroups;
    }
}
