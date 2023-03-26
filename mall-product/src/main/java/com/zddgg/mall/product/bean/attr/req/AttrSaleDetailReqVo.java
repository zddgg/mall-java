package com.zddgg.mall.product.bean.attr.req;

import com.zddgg.mall.common.request.PaginationReq;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttrSaleDetailReqVo extends PaginationReq {

    private String attrId;

    private String attrName;
}
