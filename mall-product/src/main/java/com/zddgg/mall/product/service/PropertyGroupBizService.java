package com.zddgg.mall.product.service;

import com.zddgg.mall.product.entity.AttrGroup;

import java.util.List;

public interface PropertyGroupBizService {

    List<AttrGroup> getListAndRelatedByGroupIds(List<String> groupNos);
}
