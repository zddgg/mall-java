package com.zddgg.mall.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.attr.req.*;
import com.zddgg.mall.product.bean.attr.resp.AttrUnitRecordRespVo;
import com.zddgg.mall.product.biz.AttrUnitBizService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("attr/unit")
@RequiredArgsConstructor
public class AttrUnitController {

    private final AttrUnitBizService attrUnitBizService;

    @PostMapping("page")
    public Result<Page<AttrUnitRecordRespVo>> page(@RequestBody AttrUnitRecordPageReqVo req) {
        return Result.success(attrUnitBizService.getAttrUnitRecordPage(req));
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody @Validated AttrUnitCreateReqVo reqVo) {
        attrUnitBizService.create(reqVo);
        return Result.success();
    }

    @PostMapping("detail")
    public Result<AttrUnitRecordRespVo> detail(@RequestBody @Validated AttrUnitDetailReqVo req) {
        return Result.success(attrUnitBizService.detail(req));
    }

    @PostMapping("update")
    public Result<Object> update(@RequestBody @Validated AttrUnitUpdateReqVo vo) {
        attrUnitBizService.update(vo);
        return Result.success();
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody @Validated AttrUnitDeleteReqVo reqVo) {
        attrUnitBizService.delete(reqVo);
        return Result.success();
    }
}
