package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.product.bean.PropertyGroupCreateVo;
import com.zddgg.mall.product.bean.PropertyGroupDetailReqVo;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.AttrUnitKey;
import com.zddgg.mall.product.entity.PropertyGroup;
import com.zddgg.mall.product.entity.PropertyGroupUnit;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.AttrGroupUnitService;
import com.zddgg.mall.product.service.PropertyGroupBizService;
import com.zddgg.mall.product.service.PropertyGroupService;
import com.zddgg.mall.product.service.PropertyUnitBizService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PropertyGroupBizServiceImpl implements PropertyGroupBizService {

    private final PropertyGroupService propertyGroupService;

    private final AttrGroupUnitService attrGroupUnitService;

    private final PropertyUnitBizService propertyUnitBizService;

    public PropertyGroupBizServiceImpl(PropertyGroupService propertyGroupService,
                                       AttrGroupUnitService attrGroupUnitService,
                                       PropertyUnitBizService propertyUnitBizService) {
        this.propertyGroupService = propertyGroupService;
        this.attrGroupUnitService = attrGroupUnitService;
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
        for (AttrUnitKey attrUnitKey : vo.getAttrUnitKeys()) {
            PropertyGroupUnit value = new PropertyGroupUnit();
            value.setPropertyGroupId(propertyGroup.getPropertyGroupId());
            value.setUnitKeyId(attrUnitKey.getAttrId());
            value.setPropertyOrder(0);
            saveList.add(value);
        }
        attrGroupUnitService.saveBatch(saveList);
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

        attrGroupUnitService.remove(
                new LambdaQueryWrapper<PropertyGroupUnit>()
                        .eq(PropertyGroupUnit::getPropertyGroupId, one.getPropertyGroupId()));

        ArrayList<PropertyGroupUnit> saveList = new ArrayList<>();
        for (AttrUnitKey attrUnitKey : vo.getAttrUnitKeys()) {
            PropertyGroupUnit value = new PropertyGroupUnit();
            value.setPropertyGroupId(one.getPropertyGroupId());
            value.setUnitKeyId(attrUnitKey.getAttrId());
            value.setPropertyOrder(0);
            saveList.add(value);
        }
        attrGroupUnitService.saveBatch(saveList);
    }

    @Transactional
    @Override
    public void delete(PropertyGroupDetailReqVo reqVo) {
        propertyGroupService.remove(
                new LambdaQueryWrapper<PropertyGroup>()
                        .eq(PropertyGroup::getPropertyGroupId, reqVo.getPropertyGroupId()));
        attrGroupUnitService.remove(
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
        List<PropertyGroupUnit> propertyGroupUnits = attrGroupUnitService.list(
                new LambdaQueryWrapper<PropertyGroupUnit>()
                        .in(PropertyGroupUnit::getPropertyGroupId, groupIds));
        if (!CollectionUtils.isEmpty(propertyGroupUnits)) {
            List<String> storeNos = propertyGroupUnits.stream()
                    .map(PropertyGroupUnit::getUnitKeyId).collect(Collectors.toList());
            Map<String, List<String>> group2StoreMap = propertyGroupUnits.stream()
                    .collect(Collectors.groupingBy(PropertyGroupUnit::getPropertyGroupId,
                            Collectors.mapping(PropertyGroupUnit::getUnitKeyId, Collectors.toList())));
            List<AttrUnitKey> AttrUnitKeys = propertyUnitBizService.getListAndRelatedByPropertyIds(storeNos);
            Map<String, AttrUnitKey> storeMap = AttrUnitKeys
                    .stream()
                    .collect(Collectors.toMap(AttrUnitKey::getAttrId,
                            propertyStoreRespVo -> propertyStoreRespVo));
            propertyGroups.forEach(propertyGroup -> {
                List<AttrUnitKey> storeKeys = group2StoreMap
                        .getOrDefault(propertyGroup.getPropertyGroupId(), new ArrayList<>())
                        .stream().map(storeMap::get).collect(Collectors.toList());
                propertyGroup.setAttrUnitKeys(storeKeys);
            });

        }
        return propertyGroups;
    }
}
