package com.zddgg.mall.product.bean.attr.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AttrUnitDetailReqVo {

    @NotBlank(message = "属性编号不能为空")
    private String attrId;

}
