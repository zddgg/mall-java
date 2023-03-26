package com.zddgg.mall.product.bean.attr.req;

import com.zddgg.mall.common.request.PaginationReq;
import lombok.Data;

@Data
public class AttrSaleRecordPageReqVo extends PaginationReq {

    private String attrId;

    private String attrName;

}
