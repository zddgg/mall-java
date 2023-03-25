package com.zddgg.mall.product.bean.attr.req;

import lombok.Data;

import java.util.List;

@Data
public class AttrUnitCreateReqVo {

    private String attrName;

    private String unit;

    private String formShowType;

    private List<AttrUnitValueVo> attrUnitValues;

    @Data
    public static class AttrUnitValueVo {

        private String attrId;

        private String attrValueName;

        private Integer attrValueOrder;
    }

}
