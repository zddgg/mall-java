package com.zddgg.mall.product.bean.category.backend.req;

import lombok.Data;

@Data
public class BackendCategoryCreateReqVo {

    private String categoryId;

    private String categoryName;

    private String parentId;

    private Integer level;
}
