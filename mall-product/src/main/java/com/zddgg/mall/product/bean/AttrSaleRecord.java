package com.zddgg.mall.product.bean;

import lombok.Data;

import java.util.List;

@Data
public class AttrSaleRecord {

    private String attrId;

    private String attrName;

    private List<AttrValue> attrValues;

    @Data
    public static class AttrValue {

        private String attrId;

        private String attrValueId;

        private String attrValueName;
    }
}
