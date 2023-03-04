package com.zddgg.mall.product.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PropertyGroupDetailReqVo {

    @NotBlank(message = "属性组编号不能为空")
    private String propertyGroupNo;
}
