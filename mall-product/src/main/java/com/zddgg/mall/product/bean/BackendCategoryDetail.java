package com.zddgg.mall.product.bean;

import com.zddgg.mall.product.entity.AttrGroup;
import com.zddgg.mall.product.entity.AttrSaleKey;
import com.zddgg.mall.product.entity.AttrUnitKey;
import lombok.Data;

import java.util.List;

@Data
public class BackendCategoryDetail {
    private String categoryId;

    private String categoryName;

    private String parentId;

    private Integer level;

    private Boolean relatedProperty;

    private List<AttrGroup> attrGroups;

    private List<AttrUnitKey> AttrUnitKeys;

    private List<AttrSaleKey> attrSaleKeys;
}