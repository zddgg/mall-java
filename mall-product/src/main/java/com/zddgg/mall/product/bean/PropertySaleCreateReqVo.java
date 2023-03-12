package com.zddgg.mall.product.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PropertySaleCreateReqVo {

    @NotBlank(message = "销售属性名称不能为空")
    private String keyName;
}
