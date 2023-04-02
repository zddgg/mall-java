package com.zddgg.mall.product.bean.sku;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SkuDeleteReqVo {

    @NotBlank(message = "skuId不能为空!")
    private String skuId;
}
