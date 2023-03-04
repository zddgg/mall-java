package com.zddgg.mall.product.bean;

import com.zddgg.mall.product.entity.PropertyUnitKey;
import lombok.Data;

import java.util.List;

@Data
public class PropertyGroupCreateVo {

    private String propertyGroupNo;

    private String propertyGroupName;

    private List<PropertyUnitKey> propertyStoreList;
}
