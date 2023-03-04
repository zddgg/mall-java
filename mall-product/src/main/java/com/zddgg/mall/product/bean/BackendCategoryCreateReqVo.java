package com.zddgg.mall.product.bean;

import com.zddgg.mall.product.entity.PropertyGroup;
import com.zddgg.mall.product.entity.PropertyUnitKey;
import lombok.Data;

import java.util.List;

@Data
public class BackendCategoryCreateReqVo {

    private String categoryId;

    private String categoryName;

    private String parentId;

    private Boolean relatedProperty;

    private List<PropertyGroup> propertyGroupList;

    private List<PropertyUnitKey> propertyStoreList;
}
