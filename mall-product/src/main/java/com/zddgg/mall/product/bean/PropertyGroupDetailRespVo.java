package com.zddgg.mall.product.bean;

import com.voidtime.mall.product.entity.PropertyUnitKey;
import lombok.Data;

import java.util.List;

@Data
public class PropertyGroupDetailRespVo {

    private String propertyGroupNo;

    private String propertyGroupName;

    private List<PropertyUnitKey> propertyStoreList;
}
