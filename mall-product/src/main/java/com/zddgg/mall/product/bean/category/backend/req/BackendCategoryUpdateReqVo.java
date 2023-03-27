package com.zddgg.mall.product.bean.category.backend.req;

import lombok.Data;

import java.util.List;

@Data
public class BackendCategoryUpdateReqVo {

    private String categoryId;

    private String categoryName;

    private String parentId;

    private Integer level;

    private Boolean relatedProperty;

    private List<String> propertyUnitIds;

    private List<String> propertyGroupIds;

    private List<String> propertySaleIds;
}
