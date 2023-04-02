package com.zddgg.mall.product.bean.attr.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AttrGroupDetailReqVo {

    @NotBlank(message = "属性组编号不能为空")
    private String groupId;
}
