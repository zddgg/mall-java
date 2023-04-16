package com.zddgg.mall.user.controller;

import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.common.utils.IdUtil;
import com.zddgg.mall.common.utils.JwtUtil;
import com.zddgg.mall.notify.api.request.SmsCodeVerifyReq;
import com.zddgg.mall.notify.api.service.SmsRpcService;
import com.zddgg.mall.user.bean.req.LoginReqVO;
import com.zddgg.mall.user.bean.resp.UserLoginRespVO;
import com.zddgg.mall.user.exception.BizException;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @DubboReference
    private SmsRpcService smsRpcService;

    @PostMapping("loginBySmsCode")
    public Result<UserLoginRespVO> login(@RequestBody LoginReqVO reqVO) {
        SmsCodeVerifyReq codeVerifyReq = SmsCodeVerifyReq
                .builder()
                .mobile(reqVO.getMobile())
                .bizId(reqVO.getBizId())
                .code(reqVO.getAuthCode())
                .build();
        Boolean result = smsRpcService.verifyCode(codeVerifyReq);
        if (!result) {
            throw new BizException("验证码错误！");
        } else {
            String userToken = IdUtil.getId();
            Map<String, Object> map = new HashMap<>();
            map.put("userId", "123456");
            map.put("token", userToken);
            String token = JwtUtil.createToken(map);

            UserLoginRespVO respVO = new UserLoginRespVO();
            respVO.setUserId("123455");
            respVO.setUsername("测试测试");
            respVO.setAvatar("https://img12.360buyimg.com/imagetools/jfs/t1/196430/38/8105/14329/60c806a4Ed506298a/e6de9fb7b8490f38.png");
            respVO.setToken(token);
            return Result.success(respVO);
        }
    }

    @PostMapping("logout")
    public Result<Object> logout() {
        return Result.success();
    }
}
