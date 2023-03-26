package com.zddgg.mall.product.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.product.bean.attr.req.AttrSaleCreateReqVo;
import com.zddgg.mall.product.bean.attr.req.AttrSaleDetailReqVo;
import com.zddgg.mall.product.bean.attr.req.AttrSaleRecordPageReqVo;
import com.zddgg.mall.product.bean.attr.resp.AttrSaleRecordRespVo;

public interface AttrSaleBizService {

    Page<AttrSaleRecordRespVo> getAttrSaleRecordPage(AttrSaleRecordPageReqVo req);

    void create(AttrSaleCreateReqVo req);

    AttrSaleRecordRespVo detail(AttrSaleDetailReqVo req);
}
