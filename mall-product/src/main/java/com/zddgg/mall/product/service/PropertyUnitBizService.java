package com.zddgg.mall.product.service;

import com.voidtime.mall.product.bean.PropertyUnitCreateReqVo;
import com.voidtime.mall.product.bean.PropertyUnitDeleteReqVo;
import com.voidtime.mall.product.entity.PropertyUnitKey;

import java.util.List;

public interface PropertyUnitBizService {

    void saveKeyAndValue(PropertyUnitCreateReqVo vo);

    void edit(PropertyUnitCreateReqVo vo);

    void delete(PropertyUnitDeleteReqVo reqVo);

    List<PropertyUnitKey> getListAndRelatedByPropertyNos(List<String> propertyNos);

    PropertyUnitKey getDetailAndRelatedByPropertyNo(String propertyNo);
}
