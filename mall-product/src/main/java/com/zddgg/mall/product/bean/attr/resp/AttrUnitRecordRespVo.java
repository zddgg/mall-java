package com.zddgg.mall.product.bean.attr.resp;

import lombok.Data;

import java.util.List;

@Data
public class AttrUnitRecordRespVo {

    private String attrId;

    private String attrName;

    private String unit;

    private String formShowType;

    private String status;

    private List<AttrUnitValueVo> attrUnitValues;

    @Data
    public static class AttrUnitValueVo {

        private String attrId;

        private String attrValueName;

        private Integer attrValueOrder;
    }
}
