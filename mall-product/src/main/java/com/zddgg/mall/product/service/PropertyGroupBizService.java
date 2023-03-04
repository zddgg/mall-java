package com.zddgg.mall.product.service;

import com.voidtime.mall.product.bean.PropertyGroupCreateVo;
import com.voidtime.mall.product.bean.PropertyGroupDetailReqVo;
import com.voidtime.mall.product.entity.PropertyGroup;

import java.util.List;

public interface PropertyGroupBizService {

    void saveKeyAndValue(PropertyGroupCreateVo vo);

    void edit(PropertyGroupCreateVo vo);

    void delete(PropertyGroupDetailReqVo reqVo);

    List<PropertyGroup> getListAndRelatedByGroupNos(List<String> groupNos);
}
