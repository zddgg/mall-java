package com.zddgg.mall.product.bean.sku;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SkuDeleteReqVo {

    @NotBlank(message = "skuId不能为空!")
    private String skuId;
}
