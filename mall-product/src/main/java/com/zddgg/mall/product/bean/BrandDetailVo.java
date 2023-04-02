package com.zddgg.mall.product.bean;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BrandDetailVo {

    @NotBlank(message = "品牌编号不能为空")
    private String brandId;

    private String brandName;

    private String foreignBrandName;

    private String showName;

    private String logo;

    private String level;

    private String trademarkName;

    private String status;
}
