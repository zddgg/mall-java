package com.zddgg.mall.product.api.request;

import lombok.Data;

import java.util.List;

@Data
public class GoodsBatchQueryReq {

    private List<String> skuIds;
}
