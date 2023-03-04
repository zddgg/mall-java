package com.zddgg.mall.product.bean;

import lombok.Data;

@Data
public class BackendCategoryOptionRespVo {

    private String categoryId;

    private String categoryName;

    private String parentId;

    private Integer level;
}
