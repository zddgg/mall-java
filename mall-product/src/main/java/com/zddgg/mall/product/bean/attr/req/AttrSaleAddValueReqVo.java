package com.zddgg.mall.product.bean.attr.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AttrSaleAddValueReqVo {

    @NotBlank(message = "属性KeyId不能为空！")
    private String attrId;

    @NotBlank(message = "属性valueName不能为空！")
    private String attrValueName;
}
