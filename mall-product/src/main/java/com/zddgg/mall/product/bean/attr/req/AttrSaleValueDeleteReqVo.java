package com.zddgg.mall.product.bean.attr.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttrSaleValueDeleteReqVo {

    @NotBlank(message = "属性attrId不能为空！")
    private String attrId;

    @NotBlank(message = "属性attrValueId不能为空！")
    private String attrValueId;
}
