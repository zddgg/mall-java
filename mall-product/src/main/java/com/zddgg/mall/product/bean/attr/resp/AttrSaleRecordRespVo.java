package com.zddgg.mall.product.bean.attr.resp;

import lombok.Data;

import java.util.List;

@Data
public class AttrSaleRecordRespVo {

    private String attrId;

    private String attrName;

    private List<AttrSaleValueVo> attrSaleValues;

    @Data
    public static class AttrSaleValueVo {

        private String attrId;

        private String attrValueId;

        private String attrValueName;
    }
}
