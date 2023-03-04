package com.zddgg.mall.product.bean;

import com.voidtime.mall.common.request.PaginationReq;
import lombok.Data;

@Data
public class PropertyGroupListReqVo extends PaginationReq {

    private String propertyGroupNo;

    private String propertyGroupName;

    private String status;
}
