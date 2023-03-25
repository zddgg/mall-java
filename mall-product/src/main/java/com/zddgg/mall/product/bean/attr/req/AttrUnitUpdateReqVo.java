package com.zddgg.mall.product.bean.attr.req;

import com.zddgg.mall.product.bean.attr.resp.AttrUnitRecordRespVo;
import lombok.Data;

import java.util.List;

@Data
public class AttrUnitUpdateReqVo {

    private String attrId;

    private String attrName;

    private String unit;

    private String formShowType;

    private String status;

    private List<AttrUnitRecordRespVo.AttrUnitValueVo> attrUnitValues;

    @Data
    public static class AttrUnitValueVo {

        private String attrId;

        private String attrValueName;

        private Integer attrValueOrder;
    }
}
