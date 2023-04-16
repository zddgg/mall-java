package com.zddgg.mall.store.api.service;

import com.zddgg.mall.store.api.response.StoreInfo;

import java.util.List;

public interface StoreQueryService {

    List<StoreInfo> batchQuery(List<String> storeIds);
}
