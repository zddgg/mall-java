package com.zddgg.mall.product.api.service;

import com.zddgg.mall.product.api.response.SkuInfo;

import java.util.List;

public interface SkuQueryService {

    List<SkuInfo> batchQuery(List<String> skuIds);

    SkuInfo queryBySkuId(String skuId);
}
