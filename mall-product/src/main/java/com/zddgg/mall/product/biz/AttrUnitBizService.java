package com.zddgg.mall.product.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.product.bean.attr.req.*;
import com.zddgg.mall.product.bean.attr.resp.AttrUnitRecordRespVo;
import com.zddgg.mall.product.entity.AttrUnitKey;

import java.util.List;

public interface AttrUnitBizService {

    Page<AttrUnitRecordRespVo> getAttrUnitRecordPage(AttrUnitRecordPageReqVo req);

    AttrUnitRecordRespVo detail(AttrUnitDetailReqVo req);

    void create(AttrUnitCreateReqVo req);

    void update(AttrUnitUpdateReqVo req);

    void delete(AttrUnitDeleteReqVo req);

    List<AttrUnitRecordRespVo> getRecordListByAttrUnitKeys(List<AttrUnitKey> unitKeyList);

    List<AttrUnitRecordRespVo> getRecordListByAttrIds(List<String> attrIds);

}
