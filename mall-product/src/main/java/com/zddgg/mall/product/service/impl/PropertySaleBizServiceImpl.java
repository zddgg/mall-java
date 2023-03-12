package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.product.entity.PropertySaleKey;
import com.zddgg.mall.product.entity.PropertySaleValue;
import com.zddgg.mall.product.service.PropertySaleBizService;
import com.zddgg.mall.product.service.PropertySaleKeyService;
import com.zddgg.mall.product.service.PropertySaleValueService;
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

    private final PropertySaleKeyService propertySaleKeyService;

    private final PropertySaleValueService propertySaleValueService;

    @Override
    public List<PropertySaleKey> getListAndRelatedByPropertyIds(List<String> propertyIds) {
        if (CollectionUtils.isEmpty(propertyIds)) {
            return new ArrayList<>();
        }
        List<PropertySaleKey> propertySaleKeys = propertySaleKeyService.list(
                new LambdaQueryWrapper<PropertySaleKey>()
                        .in(PropertySaleKey::getKeyId, propertyIds));
        List<PropertySaleValue> propertySaleValues = propertySaleValueService.list(
                new LambdaQueryWrapper<PropertySaleValue>()
                        .in(PropertySaleValue::getKeyId, propertyIds));
        Map<String, List<PropertySaleValue>> propertyKeyNoMap = propertySaleValues.stream()
                .collect(Collectors.groupingBy(PropertySaleValue::getKeyId));
        propertySaleKeys.forEach(propertyStoreKey ->
                propertyStoreKey.setPropertySaleValues(propertyKeyNoMap.get(propertyStoreKey.getKeyId())));
        return propertySaleKeys;
    }
}
