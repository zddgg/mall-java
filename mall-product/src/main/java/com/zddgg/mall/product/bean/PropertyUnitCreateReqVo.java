package com.zddgg.mall.product.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class PropertyUnitCreateReqVo {

    private String unitKeyId;

    @NotBlank(message = "属性名称不能为空")
    private String unitKeyName;

    private String unitKeyUnit;

    private String formShowType;

    private List<PropertyUnitValue> propertyUnitValues;

    @Data
    public static class PropertyUnitValue {

        private String unitKeyId;

        private String unitValue;

        private Integer unitValueOrder;

        private Boolean confirmed;
    }
}
