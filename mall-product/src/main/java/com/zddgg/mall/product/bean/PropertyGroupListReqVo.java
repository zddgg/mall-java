package com.zddgg.mall.product.bean;

import com.zddgg.mall.common.request.PaginationReq;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyGroupListReqVo extends PaginationReq {

    private String propertyGroupNo;

    private String propertyGroupName;

    private String status;
}
