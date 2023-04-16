package com.zddgg.mall.user.bean.req;

import lombok.Data;

@Data
public class LoginReqVO {

    private String mobile;

    private String bizId;

    private String authCode;
}
