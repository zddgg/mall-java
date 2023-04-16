package com.zddgg.mall.notify.api.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@Accessors(chain = true)
public class SmsCodeVerifyReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String bizId;

    private String mobile;

    private String code;
}
