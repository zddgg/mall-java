package com.zddgg.mall.product.bean;

import lombok.Data;

import java.util.List;

@Data
public class SpuSkuCreateReqVo {

    private String categoryId;
    private String storeId;
    private String spuName;
    private String brandId;
    private List<String> attrSaleIds;
    private List<AttrSaleRecord> spuAttrSaleDataList;
    private List<SkuItem> skuList;


    @Data
    public static class SkuItem {
        private String skuName;
        private String retailPrice;
        private String attrList;
    }

    @Data
    public static class AttrFlatMapItem {
        private String attrId;
        private String attrName;
        private String attrValueId;
        private String attrValueName;
    }

}
