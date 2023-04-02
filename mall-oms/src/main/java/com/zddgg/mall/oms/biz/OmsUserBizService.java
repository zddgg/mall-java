package com.zddgg.mall.oms.biz;

import com.zddgg.mall.oms.vo.req.OmsUserCreateReqVO;
import com.zddgg.mall.oms.vo.req.OmsUserLoginReqVO;
import com.zddgg.mall.oms.vo.resp.OmsUserLoginRespVO;

public interface OmsUserBizService {

    void create(OmsUserCreateReqVO reqVO);

    OmsUserLoginRespVO login(OmsUserLoginReqVO reqVO);
}
