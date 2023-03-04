package com.zddgg.mall.product.bean;

import lombok.Data;

@Data
public class BrandCreateVo {

    private String brandName;

    /**
     * 外文品牌名称
     */
    private String foreignBrandName;

    /**
     * 品牌展示名称
     */
    private String showName;

    /**
     * 品牌LOGO图片URL
     */
    private String logo;
}
