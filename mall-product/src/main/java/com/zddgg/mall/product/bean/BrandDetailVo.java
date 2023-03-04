package com.zddgg.mall.product.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BrandDetailVo {

    @NotBlank(message = "品牌编号不能为空")
    private String brandId;

    private String brandName;

    private String foreignBrandName;

    private String showName;

    private String logo;

    private String level;

    private String trademarkName;

    private String status;
}
