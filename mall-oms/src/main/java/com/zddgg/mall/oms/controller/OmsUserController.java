package com.zddgg.mall.oms.controller;

import com.alibaba.fastjson2.JSON;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.oms.biz.OmsUserBizService;
import com.zddgg.mall.oms.vo.req.OmsUserCreateReqVO;
import com.zddgg.mall.oms.vo.req.OmsUserLoginReqVO;
import com.zddgg.mall.oms.vo.resp.OmsUserInfoRespVO;
import com.zddgg.mall.oms.vo.resp.OmsUserLoginRespVO;
import com.zddgg.mall.oms.vo.resp.RouteRecordRespVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class OmsUserController {

    private final OmsUserBizService omsUserBizService;

    @PostMapping("create")
    public Result<Object> create(@RequestBody OmsUserCreateReqVO reqVO) {
        omsUserBizService.create(reqVO);
        return Result.success();
    }

    @PostMapping("login")
    public Result<OmsUserLoginRespVO> login(@RequestBody OmsUserLoginReqVO reqVO) {
        return Result.success(omsUserBizService.login(reqVO)).setMsg("登录成功");
    }

    @PostMapping("info")
    public Result<OmsUserInfoRespVO> info() {
        return Result.success(JSON.parseObject(userInfo, OmsUserInfoRespVO.class));
    }

    @PostMapping("logout")
    public Result<OmsUserInfoRespVO> logout() {
        return Result.success();
    }

    @PostMapping("menu")
    public Result<List<RouteRecordRespVO>> menu() {
        List<RouteRecordRespVO> menus = JSON.parseArray(menuList, RouteRecordRespVO.class);
        return Result.success(menus);
    }

    static String userInfo = "{\n" +
            "  \"name\": \"王立群\",\n" +
            "  \"avatar\": \"//lf1-xgcdn-tos.pstatp.com/obj/vcloud/vadmin/start.8e0e4855ee346a46ccff8ff3e24db27b.png\",\n" +
            "  \"email\": \"wangliqun@email.com\",\n" +
            "  \"job\": \"frontend\",\n" +
            "  \"jobName\": \"前端艺术家\",\n" +
            "  \"organization\": \"Frontend\",\n" +
            "  \"organizationName\": \"前端\",\n" +
            "  \"location\": \"beijing\",\n" +
            "  \"locationName\": \"北京\",\n" +
            "  \"introduction\": \"人潇洒，性温存\",\n" +
            "  \"personalWebsite\": \"https://www.arco.design\",\n" +
            "  \"phone\": \"150****0000\",\n" +
            "  \"registrationDate\": \"2013-05-10 12:10:00\",\n" +
            "  \"accountId\": \"15012312300\",\n" +
            "  \"certification\": 1,\n" +
            "  \"role\": \"admin\"\n" +
            "}";

    static String menuList = "[\n" +
            "  {\n" +
            "    \"path\": \"/dashboard\",\n" +
            "    \"name\": \"dashboard\",\n" +
            "    \"meta\": {\n" +
            "      \"locale\": \"menu.server.dashboard\",\n" +
            "      \"requiresAuth\": true,\n" +
            "      \"icon\": \"icon-dashboard\",\n" +
            "      \"order\": 1\n" +
            "    },\n" +
            "    \"children\": [\n" +
            "      {\n" +
            "        \"path\": \"workplace\",\n" +
            "        \"name\": \"Workplace\",\n" +
            "        \"meta\": {\n" +
            "          \"locale\": \"menu.server.workplace\",\n" +
            "          \"requiresAuth\": true\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"path\": \"https://arco.design\",\n" +
            "        \"name\": \"arcoWebsite\",\n" +
            "        \"meta\": {\n" +
            "          \"locale\": \"menu.arcoWebsite\",\n" +
            "          \"requiresAuth\": true\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]";
}
