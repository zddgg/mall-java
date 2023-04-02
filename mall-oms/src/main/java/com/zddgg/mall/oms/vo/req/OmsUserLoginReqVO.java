package com.zddgg.mall.oms.vo.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OmsUserLoginReqVO {

    @NotBlank(message = "用户编号不能为空!")
    private String username;

    @NotBlank(message = "密码不能为空!")
    private String password;
}
