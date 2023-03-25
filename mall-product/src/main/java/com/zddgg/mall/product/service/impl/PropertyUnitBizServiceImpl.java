package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.product.entity.AttrUnitKey;
import com.zddgg.mall.product.entity.AttrUnitValue;
import com.zddgg.mall.product.service.AttrUnitKeyService;
import com.zddgg.mall.product.service.AttrUnitValueService;
import com.zddgg.mall.product.service.PropertyUnitBizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyUnitBizServiceImpl implements PropertyUnitBizService {

    private final AttrUnitKeyService attrUnitKeyService;

    private final AttrUnitValueService attrUnitValueService;


    @Override
    public List<AttrUnitKey> getListAndRelatedByPropertyIds(List<String> propertyKeyNos) {
        if (CollectionUtils.isEmpty(propertyKeyNos)) {
            return new ArrayList<>();
        }
        List<AttrUnitKey> AttrUnitKeys = attrUnitKeyService.list(
                new LambdaQueryWrapper<AttrUnitKey>()
                        .in(AttrUnitKey::getAttrId, propertyKeyNos));
        List<AttrUnitValue> attrUnitValues = attrUnitValueService.list(
                new LambdaQueryWrapper<AttrUnitValue>()
                        .in(AttrUnitValue::getAttrId, propertyKeyNos));
        Map<String, List<AttrUnitValue>> propertyKeyNoMap = attrUnitValues.stream()
                .collect(Collectors.groupingBy(AttrUnitValue::getAttrId));
        AttrUnitKeys.forEach(propertyStoreKey ->
                propertyStoreKey.setAttrUnitValues(propertyKeyNoMap.get(propertyStoreKey.getAttrId())));
        return AttrUnitKeys;
    }
}
