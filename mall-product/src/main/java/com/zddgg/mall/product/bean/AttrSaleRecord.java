package com.zddgg.mall.product.bean;

import lombok.Data;

import java.util.List;

@Data
public class AttrSaleRecord {

    private String keyId;

    private String keyName;

    private List<AttrValue> propertySaleValues;

    @Data
    public static class AttrValue {

        private String keyId;

        private String valueId;

        private String valueName;
    }
}
