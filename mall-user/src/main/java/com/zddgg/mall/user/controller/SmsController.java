package com.zddgg.mall.user.controller;

import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.common.utils.IdUtil;
import com.zddgg.mall.notify.api.request.SmsCodeSendReq;
import com.zddgg.mall.notify.api.service.SmsRpcService;
import com.zddgg.mall.user.bean.req.SmsReqVO;
import com.zddgg.mall.user.bean.resp.SmsCodeSendResp;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sms")
public class SmsController {

    @DubboReference
    private SmsRpcService smsRpcService;

    @PostMapping("sendCode")
    public Result<SmsCodeSendResp> sendCode(@RequestBody @Validated SmsReqVO smsReqVO) {
        // 生成业务编号
        String loginBizId = IdUtil.getId();
        String mobile = smsReqVO.getMobile();
        SmsCodeSendReq codeSendReq = SmsCodeSendReq
                .builder()
                .bizId(loginBizId)
                .mobile(mobile)
                .build();
        smsRpcService.sendCode(codeSendReq);
        return Result.success(SmsCodeSendResp.builder().bizId(loginBizId).build());
    }
}
