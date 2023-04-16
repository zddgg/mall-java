package com.zddgg.mall.store.api.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@Accessors(chain = true)
public class StoreInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String storeId;

    private String storeName;
}
