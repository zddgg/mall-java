package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.product.entity.AttrSaleKey;
import com.zddgg.mall.product.entity.AttrSaleValue;
import com.zddgg.mall.product.service.AttrSaleKeyService;
import com.zddgg.mall.product.service.AttrSaleValueService;
import com.zddgg.mall.product.service.PropertySaleBizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertySaleBizServiceImpl implements PropertySaleBizService {

    private final AttrSaleKeyService attrSaleKeyService;

    private final AttrSaleValueService attrSaleValueService;

    @Override
    public List<AttrSaleKey> getListAndRelatedByPropertyIds(List<String> propertyIds) {
        if (CollectionUtils.isEmpty(propertyIds)) {
            return new ArrayList<>();
        }
        List<AttrSaleKey> attrSaleKeys = attrSaleKeyService.list(
                new LambdaQueryWrapper<AttrSaleKey>()
                        .in(AttrSaleKey::getAttrId, propertyIds));
        List<AttrSaleValue> attrSaleValues = attrSaleValueService.list(
                new LambdaQueryWrapper<AttrSaleValue>()
                        .in(AttrSaleValue::getAttrId, propertyIds));
        Map<String, List<AttrSaleValue>> propertyKeyNoMap = attrSaleValues.stream()
                .collect(Collectors.groupingBy(AttrSaleValue::getAttrId));
        attrSaleKeys.forEach(propertyStoreKey ->
                propertyStoreKey.setAttrSaleValues(propertyKeyNoMap.get(propertyStoreKey.getAttrId())));
        return attrSaleKeys;
    }
}
