package com.zddgg.mall.product.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class PropertySaleDetailReqVo {

    private String keyId;

    private String keyName;

    private List<PropertySaleValue> propertySaleValues;

    @Data
    public static class PropertySaleValue {

        private String saleKeyId;

        private String propertyValue;

        private Integer propertyOrder;

        private Boolean confirmed;
    }
}
