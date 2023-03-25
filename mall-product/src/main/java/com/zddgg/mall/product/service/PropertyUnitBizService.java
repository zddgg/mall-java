package com.zddgg.mall.product.service;

import com.zddgg.mall.product.entity.AttrUnitKey;

import java.util.List;

public interface PropertyUnitBizService {

    List<AttrUnitKey> getListAndRelatedByPropertyIds(List<String> propertyNos);
}
