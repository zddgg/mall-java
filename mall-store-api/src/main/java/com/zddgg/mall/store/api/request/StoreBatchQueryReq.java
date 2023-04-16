package com.zddgg.mall.store.api.request;

import lombok.Data;

import java.util.List;

@Data
public class StoreBatchQueryReq {

    private List<String> storeIds;
}
