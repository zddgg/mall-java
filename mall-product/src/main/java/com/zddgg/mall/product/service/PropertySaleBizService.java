package com.zddgg.mall.product.service;

import com.zddgg.mall.product.entity.AttrSaleKey;

import java.util.List;

public interface PropertySaleBizService {

    List<AttrSaleKey> getListAndRelatedByPropertyIds(List<String> propertyIds);
}
