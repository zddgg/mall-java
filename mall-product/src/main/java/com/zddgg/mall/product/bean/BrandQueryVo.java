package com.zddgg.mall.product.bean;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BrandQueryVo {

    @NotBlank(message = "品牌编号不能为空")
    private String brandId;
}
