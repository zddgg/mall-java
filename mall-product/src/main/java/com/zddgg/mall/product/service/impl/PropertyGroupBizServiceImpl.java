package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.product.entity.AttrGroup;
import com.zddgg.mall.product.entity.AttrGroupUnit;
import com.zddgg.mall.product.entity.AttrUnitKey;
import com.zddgg.mall.product.service.AttrGroupService;
import com.zddgg.mall.product.service.AttrGroupUnitService;
import com.zddgg.mall.product.service.PropertyGroupBizService;
import com.zddgg.mall.product.service.PropertyUnitBizService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PropertyGroupBizServiceImpl implements PropertyGroupBizService {

    private final AttrGroupService attrGroupService;

    private final AttrGroupUnitService attrGroupUnitService;

    private final PropertyUnitBizService propertyUnitBizService;

    public PropertyGroupBizServiceImpl(AttrGroupService attrGroupService,
                                       AttrGroupUnitService attrGroupUnitService,
                                       PropertyUnitBizService propertyUnitBizService) {
        this.attrGroupService = attrGroupService;
        this.attrGroupUnitService = attrGroupUnitService;
        this.propertyUnitBizService = propertyUnitBizService;
    }

    @Override
    public List<AttrGroup> getListAndRelatedByGroupIds(List<String> groupIds) {
        if (CollectionUtils.isEmpty(groupIds)) {
            return new ArrayList<>();
        }
        List<AttrGroup> attrGroups = attrGroupService.list(
                new LambdaQueryWrapper<AttrGroup>()
                        .in(AttrGroup::getGroupId, groupIds));
        List<AttrGroupUnit> attrGroupUnits = attrGroupUnitService.list(
                new LambdaQueryWrapper<AttrGroupUnit>()
                        .in(AttrGroupUnit::getGroupId, groupIds));
        if (!CollectionUtils.isEmpty(attrGroupUnits)) {
            List<String> storeNos = attrGroupUnits.stream()
                    .map(AttrGroupUnit::getAttrId).collect(Collectors.toList());
            Map<String, List<String>> group2StoreMap = attrGroupUnits.stream()
                    .collect(Collectors.groupingBy(AttrGroupUnit::getGroupId,
                            Collectors.mapping(AttrGroupUnit::getAttrId, Collectors.toList())));
            List<AttrUnitKey> AttrUnitKeys = propertyUnitBizService.getListAndRelatedByPropertyIds(storeNos);
            Map<String, AttrUnitKey> storeMap = AttrUnitKeys
                    .stream()
                    .collect(Collectors.toMap(AttrUnitKey::getAttrId,
                            propertyStoreRespVo -> propertyStoreRespVo));
            attrGroups.forEach(propertyGroup -> {
                List<AttrUnitKey> storeKeys = group2StoreMap
                        .getOrDefault(propertyGroup.getGroupId(), new ArrayList<>())
                        .stream().map(storeMap::get).collect(Collectors.toList());
                propertyGroup.setAttrUnitKeys(storeKeys);
            });

        }
        return attrGroups;
    }
}
