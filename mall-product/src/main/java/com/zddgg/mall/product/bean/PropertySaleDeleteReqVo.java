package com.zddgg.mall.product.bean;

import com.zddgg.mall.common.request.PaginationReq;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PropertySaleDeleteReqVo extends PaginationReq {

    @NotBlank(message = "属性KeyId不能为空！")
    private String keyId;

    @NotBlank(message = "属性valueId不能为空！")
    private String valueId;
}
