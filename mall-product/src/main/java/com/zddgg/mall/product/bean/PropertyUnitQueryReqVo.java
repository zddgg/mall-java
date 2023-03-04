package com.zddgg.mall.product.bean;

import com.voidtime.mall.common.request.PaginationReq;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyUnitQueryReqVo extends PaginationReq {

    private String unitKeyId;

    private String unitKeyName;

    private String unitKeyUnit;

    private String formShowType;
}
