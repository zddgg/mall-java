package com.zddgg.mall.product.bean.attr.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AttrGroupDetailReqVo {

    @NotBlank(message = "属性组编号不能为空")
    private String groupId;
}
