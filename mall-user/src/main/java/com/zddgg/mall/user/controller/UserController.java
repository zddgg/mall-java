package com.zddgg.mall.user.controller;

import com.zddgg.mall.common.response.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @PostMapping("info")
    public Result<Object> getUserInfo() {
        return Result.success();
    }
}
