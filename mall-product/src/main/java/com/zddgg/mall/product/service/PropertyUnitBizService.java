package com.zddgg.mall.product.service;

import com.zddgg.mall.product.bean.PropertyUnitCreateReqVo;
import com.zddgg.mall.product.bean.PropertyUnitDeleteReqVo;
import com.zddgg.mall.product.entity.PropertyUnitKey;

import java.util.List;

public interface PropertyUnitBizService {

    void saveKeyAndValue(PropertyUnitCreateReqVo vo);

    void edit(PropertyUnitCreateReqVo vo);

    void delete(PropertyUnitDeleteReqVo reqVo);

    List<PropertyUnitKey> getListAndRelatedByPropertyIds(List<String> propertyNos);

    PropertyUnitKey getDetailAndRelatedByPropertyNo(String propertyNo);
}
