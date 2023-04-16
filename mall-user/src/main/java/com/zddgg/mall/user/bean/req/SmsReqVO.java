package com.zddgg.mall.user.bean.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SmsReqVO {

    @NotBlank(message = "手机号不能为空")
    private String mobile;
}
