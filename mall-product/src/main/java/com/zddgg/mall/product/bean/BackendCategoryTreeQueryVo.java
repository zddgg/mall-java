package com.zddgg.mall.product.bean;

import lombok.Data;

@Data
public class BackendCategoryTreeQueryVo {

    private Integer maxLevel;

    private Boolean rootHelp = false;
}
