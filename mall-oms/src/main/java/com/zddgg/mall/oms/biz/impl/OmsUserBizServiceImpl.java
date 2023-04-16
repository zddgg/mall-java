package com.zddgg.mall.oms.biz.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.common.utils.JwtUtil;
import com.zddgg.mall.oms.biz.OmsUserBizService;
import com.zddgg.mall.oms.constant.StateFlagEnum;
import com.zddgg.mall.oms.entity.OmsRole;
import com.zddgg.mall.oms.entity.OmsUser;
import com.zddgg.mall.oms.entity.OmsUserRole;
import com.zddgg.mall.oms.exception.BizException;
import com.zddgg.mall.oms.service.IdService;
import com.zddgg.mall.oms.service.OmsRoleService;
import com.zddgg.mall.oms.service.OmsUserRoleService;
import com.zddgg.mall.oms.service.OmsUserService;
import com.zddgg.mall.oms.utils.IdUtil;
import com.zddgg.mall.oms.vo.req.OmsUserCreateReqVO;
import com.zddgg.mall.oms.vo.req.OmsUserLoginReqVO;
import com.zddgg.mall.oms.vo.resp.OmsUserLoginRespVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OmsUserBizServiceImpl implements OmsUserBizService {

    private final IdService idService;

    private final OmsRoleService omsRoleService;

    private final OmsUserService omsUserService;

    private final OmsUserRoleService omsUserRoleService;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void create(OmsUserCreateReqVO reqVO) {
        OmsRole omsRole = omsRoleService.getOneByRoleId(reqVO.getRoleId());
        if (Objects.isNull(omsRole)) {
            throw new BizException("角色信息查询失败！");
        }
        OmsUser omsUser = new OmsUser();
        omsUser.setUserId(idService.getId());
        omsUser.setUsername(reqVO.getUsername());
        omsUser.setPassword(reqVO.getPassword());
        omsUser.setEmail(reqVO.getEmail());
        omsUser.setMobile(reqVO.getMobile());
        omsUser.setStateFlag(StateFlagEnum.ENABLED.code);
        omsUserService.save(omsUser);

        OmsUserRole omsUserRole = new OmsUserRole();
        omsUserRole.setUserId(omsUser.getUserId());
        omsUserRole.setRoleId(omsRole.getRoleId());
        omsUserRole.setStateFlag(StateFlagEnum.ENABLED.code);
        omsUserRoleService.save(omsUserRole);
    }

    @Override
    public OmsUserLoginRespVO login(OmsUserLoginReqVO reqVO) {
        BoundValueOperations<String, String> userInfoOps = stringRedisTemplate.boundValueOps("oms-user:" + reqVO.getUsername() + ":info");
        String userStr = userInfoOps.get();
        OmsUser user;
        if (StringUtils.isBlank(userStr)) {
            user = omsUserService.getOne(
                    new LambdaQueryWrapper<OmsUser>()
                            .eq(OmsUser::getUserId, reqVO.getUsername())
            );
            if (Objects.isNull(user) || !StringUtils.equals(user.getPassword(), reqVO.getPassword())) {
                throw new BizException("用户编号或密码错误");
            }
            userInfoOps.set(JSON.toJSONString(user));
        } else {
            user = JSON.parseObject(userStr, OmsUser.class);
        }

        String userToken = IdUtil.getUUID();
        stringRedisTemplate.opsForValue().set("oms-user:" + reqVO.getUsername() + ":token", userToken, 30, TimeUnit.MINUTES);

        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getUserId());
        map.put("token", userToken);
        String token = JwtUtil.createToken(map);

        OmsUserLoginRespVO respVO = new OmsUserLoginRespVO();
        respVO.setUserId(user.getUserId());
        respVO.setUsername(user.getUsername());
        respVO.setEmail(user.getEmail());
        respVO.setMobile(user.getMobile());
        respVO.setToken(token);
        return respVO;
    }
}
