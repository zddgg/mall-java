package com.zddgg.mall.cart.bean.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartStoreInfoRespVO {

    private String storeId;

    private String storeName;

    private List<CartPreferentialInfo> cartPreferentialInfos;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartPreferentialInfo {

        private String preferentialId;

        private String preferentialType;

        private String preferentialTitle;

        private List<CartSkuInfo> cartSkuInfos;
    }

    @Data
    @Builder
    public static class CartSkuInfo {

        private CartInfo cartInfo;

        private SkuInfo skuInfo;

        private ActivityInfo activityInfo;

    }

    @Data
    @Builder
    public static class CartInfo {

        private String cartId;

        private String storeId;

        private String skuId;

        private Integer skuNum;

        private BigDecimal addPrice;

        private Boolean selected;
    }

    @Data
    @Builder
    public static class SkuInfo {

        private String skuId;

        private String skuName;

        private String thumbnail;

        private String attrStr;

        private BigDecimal retailPrice;

        private Integer minLimit;

        private Integer maxLimit;
    }

    @Data
    @Builder
    public static class ActivityInfo {

        private Boolean activity;

        private String activityTypeId;

        private String activityTypeName;

        private String activityId;

        private String activityName;

        private String StartTime;

        private String endTime;
    }

}
