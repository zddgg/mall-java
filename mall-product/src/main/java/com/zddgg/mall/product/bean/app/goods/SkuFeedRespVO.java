package com.zddgg.mall.product.bean.app.goods;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class SkuFeedRespVO {

    private String spuId;

    private String skuId;

    private String skuName;

    private Double retailPrice;

    private String thumbnail;
}
