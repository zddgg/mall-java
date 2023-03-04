package com.zddgg.mall.product.bean;

import com.voidtime.mall.product.entity.PropertyGroup;
import com.voidtime.mall.product.entity.PropertyUnitKey;
import lombok.Data;

import java.util.List;

@Data
public class BackendCategoryDetailRespVo {

    private String categoryId;

    private String categoryName;

    private String parentId;

    private Integer level;

    private Boolean relatedProperty;

    private List<PropertyGroup> propertyGroupList;

    private List<PropertyUnitKey> propertyStoreList;

    private List<BackendCategoryDetailRespVo> parentCategoryDetail;
}
