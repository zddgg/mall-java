package com.zddgg.mall.product.service;

import com.zddgg.mall.product.bean.PropertyGroupCreateVo;
import com.zddgg.mall.product.bean.PropertyGroupDetailReqVo;
import com.zddgg.mall.product.entity.PropertyGroup;

import java.util.List;

public interface PropertyGroupBizService {

    void saveKeyAndValue(PropertyGroupCreateVo vo);

    void edit(PropertyGroupCreateVo vo);

    void delete(PropertyGroupDetailReqVo reqVo);

    List<PropertyGroup> getListAndRelatedByGroupNos(List<String> groupNos);
}
