package com.zddgg.mall.user.bean.resp;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class SmsCodeSendResp {

    private String bizId;
}
