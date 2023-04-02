package com.zddgg.mall.oms.vo.resp;

import lombok.Data;

@Data
public class OmsUserLoginRespVO {

    private String userId;

    private String username;

    private String email;

    private String mobile;

    private String token;
}
