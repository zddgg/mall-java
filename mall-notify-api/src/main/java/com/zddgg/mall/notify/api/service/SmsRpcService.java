package com.zddgg.mall.notify.api.service;

import com.zddgg.mall.notify.api.request.SmsCodeSendReq;
import com.zddgg.mall.notify.api.request.SmsCodeVerifyReq;

public interface SmsRpcService {

    Boolean sendCode(SmsCodeSendReq req);

    Boolean verifyCode(SmsCodeVerifyReq req);
}
