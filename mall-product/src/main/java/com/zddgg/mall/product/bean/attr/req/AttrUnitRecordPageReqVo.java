package com.zddgg.mall.product.bean.attr.req;

import com.zddgg.mall.common.request.PaginationReq;
import lombok.Data;

@Data
public class AttrUnitRecordPageReqVo extends PaginationReq {

    private String attrId;

    private String attrName;

    private String unit;

    private String formShowType;

    private String status;

}
