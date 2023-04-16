package com.zddgg.mall.notify.facade.provider.rpc;

import com.zddgg.mall.notify.api.request.SmsCodeSendReq;
import com.zddgg.mall.notify.api.request.SmsCodeVerifyReq;
import com.zddgg.mall.notify.api.service.SmsRpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@DubboService
public class SmsRpcServiceImpl implements SmsRpcService {

    public static Map<String, String> MAP = new ConcurrentHashMap<>();

    @Override
    public Boolean sendCode(SmsCodeSendReq req) {
        MAP.put(req.getBizId(), "123456");
        log.info("发送验证吗请求: [{}]", req);
        return true;
    }

    @Override
    public Boolean verifyCode(SmsCodeVerifyReq req) {
        return MAP.containsKey(req.getBizId())
                && StringUtils.equals(MAP.get(req.getBizId()), req.getCode());
    }
}
