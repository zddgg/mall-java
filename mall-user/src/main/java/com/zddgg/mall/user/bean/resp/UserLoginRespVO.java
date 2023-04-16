package com.zddgg.mall.user.bean.resp;

import lombok.Data;

@Data
public class UserLoginRespVO {

    private String userId;

    private String username;

    private String avatar;

    private String email;

    private String mobile;

    private String token;
}
