package com.zddgg.mall.product.bean.attr.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class AttrSaleCreateReqVo {

    @NotBlank(message = "销售属性名称不能为空")
    private String attrName;

    private List<AttrSaleValue> attrSaleValues;

    @Data
    public static class AttrSaleValue {
        private String attrValueName;
    }
}
