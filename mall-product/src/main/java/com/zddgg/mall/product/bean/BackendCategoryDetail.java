package com.zddgg.mall.product.bean;

import com.zddgg.mall.product.entity.PropertyGroup;
import com.zddgg.mall.product.entity.PropertySaleKey;
import com.zddgg.mall.product.entity.PropertyUnitKey;
import lombok.Data;

import java.util.List;

@Data
public class BackendCategoryDetail {
    private String categoryId;

    private String categoryName;

    private String parentId;

    private Integer level;

    private Boolean relatedProperty;

    private List<PropertyGroup> propertyGroups;

    private List<PropertyUnitKey> propertyUnitKeys;

    private List<PropertySaleKey> propertySaleKeys;
}