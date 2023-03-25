package com.zddgg.mall.product.bean;

import com.zddgg.mall.product.entity.AttrUnitKey;
import lombok.Data;

import java.util.List;

@Data
public class PropertyGroupDetailRespVo {

    private String propertyGroupId;

    private String propertyGroupName;

    private List<AttrUnitKey> AttrUnitKeys;
}
