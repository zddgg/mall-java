package com.zddgg.mall.product.controller.attr;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.attr.req.*;
import com.zddgg.mall.product.bean.attr.resp.AttrGroupRecordRespVo;
import com.zddgg.mall.product.bean.attr.resp.AttrUnitRecordRespVo;
import com.zddgg.mall.product.biz.AttrGroupBizService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("attr/group")
@RequiredArgsConstructor
public class AttrGroupController {

    private final AttrGroupBizService attrGroupBizService;

    @PostMapping("page")
    public Result<Page<AttrGroupRecordRespVo>> page(@RequestBody AttrGroupRecordPageReqVo reqVo) {
        return Result.success(attrGroupBizService.getAttrGroupRecordPage(reqVo));
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody @Validated AttrGroupCreateReqVo vo) {
        attrGroupBizService.create(vo);
        return Result.success();
    }

    @PostMapping("detail")
    public Result<AttrGroupRecordRespVo> detail(@RequestBody @Validated AttrGroupDetailReqVo req) {
        return Result.success(attrGroupBizService.detail(req));
    }

    @PostMapping("update")
    public Result<Object> update(@RequestBody @Validated AttrGroupCreateReqVo vo) {
        attrGroupBizService.update(vo);
        return Result.success();
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody @Validated AttrGroupDeleteReqVo reqVo) {
        attrGroupBizService.delete(reqVo);
        return Result.success();
    }

    @PostMapping("unBindAttrUnit")
    public Result<Object> unBindAttrUnit(@RequestBody @Validated AttrGroupBindReqVo reqVo) {
        attrGroupBizService.unBindAttrUnit(reqVo);
        return Result.success();
    }

    @PostMapping("getBindAttrUnit")
    public Result<List<AttrUnitRecordRespVo>> getBindAttrUnit(@RequestBody @Validated AttrGroupBindReqVo reqVo) {
        return Result.success(attrGroupBizService.getBindAttrUnit(reqVo));
    }
}
