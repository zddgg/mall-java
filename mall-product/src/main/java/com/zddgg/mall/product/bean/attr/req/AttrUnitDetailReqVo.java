package com.zddgg.mall.product.bean.attr.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AttrUnitDetailReqVo {

    @NotBlank(message = "属性编号不能为空")
    private String attrId;

}
