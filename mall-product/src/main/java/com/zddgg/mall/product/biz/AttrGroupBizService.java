package com.zddgg.mall.product.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.product.bean.attr.req.*;
import com.zddgg.mall.product.bean.attr.resp.AttrGroupRecordRespVo;
import com.zddgg.mall.product.bean.attr.resp.AttrUnitRecordRespVo;

import java.util.List;

public interface AttrGroupBizService {

    Page<AttrGroupRecordRespVo> getAttrGroupRecordPage(AttrGroupRecordPageReqVo req);

    void create(AttrGroupCreateReqVo req);

    AttrGroupRecordRespVo detail(AttrGroupDetailReqVo req);

    void unBindAttrUnit(AttrGroupBindReqVo req);

    void update(AttrGroupCreateReqVo req);

    List<AttrUnitRecordRespVo> getBindAttrUnit(AttrGroupBindReqVo req);

    void delete(AttrGroupDeleteReqVo req);
}
