package com.zddgg.mall.product.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.product.bean.attr.req.AttrSaleCreateReqVo;
import com.zddgg.mall.product.bean.attr.req.AttrSaleDetailReqVo;
import com.zddgg.mall.product.bean.attr.req.AttrSaleRecordPageReqVo;
import com.zddgg.mall.product.bean.attr.resp.AttrSaleRecordRespVo;
import com.zddgg.mall.product.entity.AttrSaleKey;

import java.util.List;

public interface AttrSaleBizService {

    Page<AttrSaleRecordRespVo> getAttrSaleRecordPage(AttrSaleRecordPageReqVo req);

    void create(AttrSaleCreateReqVo req);

    AttrSaleRecordRespVo detail(AttrSaleDetailReqVo req);

    List<AttrSaleRecordRespVo> getRecordListByAttrSaleKeys(List<AttrSaleKey> attrSaleKeys);

    List<AttrSaleRecordRespVo> getRecordListByAttrIds(List<String> attrIds);
}
