package com.zddgg.mall.product.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BrandQueryVo {

    @NotBlank(message = "品牌编号不能为空")
    private String brandId;
}
