package com.zddgg.mall.product.bean;

import lombok.Data;

import java.util.List;

@Data
public class BackendCategoryDetailAndParentRespVo {

    private BackendCategoryDetail detail;

    private List<BackendCategoryDetail> parentDetails;
}
