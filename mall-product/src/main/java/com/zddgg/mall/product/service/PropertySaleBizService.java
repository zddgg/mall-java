package com.zddgg.mall.product.service;

import com.zddgg.mall.product.entity.PropertySaleKey;

import java.util.List;

public interface PropertySaleBizService {

    List<PropertySaleKey> getListAndRelatedByPropertyIds(List<String> propertyIds);
}
