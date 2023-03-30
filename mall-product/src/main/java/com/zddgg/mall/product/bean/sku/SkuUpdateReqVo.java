package com.zddgg.mall.product.bean.sku;

import lombok.Data;

@Data
public class SkuUpdateReqVo {

    private String skuId;

    private String skuName;

    private Double retailPrice;
}
