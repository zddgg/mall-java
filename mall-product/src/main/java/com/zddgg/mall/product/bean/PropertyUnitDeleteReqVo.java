package com.zddgg.mall.product.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PropertyUnitDeleteReqVo {

    @NotBlank(message = "属性编号不能为空")
    private String unitKeyId;
}
