package com.zddgg.mall.product.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SkuInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String storeId;

    private String spuId;

    private String skuId;

    private String skuName;

    private BigDecimal retailPrice;

    private String thumbnail;

    private String statusFlag;
}
