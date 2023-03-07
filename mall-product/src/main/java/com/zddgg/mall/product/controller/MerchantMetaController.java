package com.zddgg.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.common.request.PaginationReq;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.MerchantMeta;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.IdService;
import com.zddgg.mall.product.service.MerchantMetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("merchant")
@RequiredArgsConstructor
public class MerchantMetaController {

    private final IdService idService;

    private final MerchantMetaService merchantMetaService;

    @PostMapping("list")
    public Result<Page<MerchantMeta>> list(@RequestBody PaginationReq req) {
        Page<MerchantMeta> page = new Page<>(req.getCurrent(), req.getPageSize());
        LambdaQueryWrapper<MerchantMeta> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(MerchantMeta::getStatusFlag, StatusEnum.DELETED.code);
        merchantMetaService.page(page, queryWrapper);
        return Result.success(page);
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody MerchantMeta reqVo) {
        MerchantMeta nameCheck = merchantMetaService.getOne(
                new LambdaQueryWrapper<MerchantMeta>()
                        .eq(MerchantMeta::getMerName, reqVo.getMerName()));
        if (Objects.nonNull(nameCheck)) {
            throw new BizException("商户名已存在！");
        }
        MerchantMeta merchantMeta = new MerchantMeta();
        merchantMeta.setMerNo(idService.getId());
        merchantMeta.setMerName(reqVo.getMerName());
        merchantMeta.setStatusFlag(StatusEnum.ENABLED.code);
        merchantMetaService.save(merchantMeta);
        return Result.success();
    }

    @PostMapping("update")
    public Result<Object> update(@RequestBody MerchantMeta reqVo) {
        MerchantMeta noCheck = merchantMetaService.getOne(
                new LambdaQueryWrapper<MerchantMeta>()
                        .eq(MerchantMeta::getMerNo, reqVo.getMerNo()));
        if (Objects.isNull(noCheck)) {
            throw new BizException("商户不存在！");
        }
        noCheck.setMerName(reqVo.getMerName());
        merchantMetaService.updateById(noCheck);
        return Result.success();
    }

    @PostMapping("detail")
    public Result<MerchantMeta> detail(@RequestBody MerchantMeta reqVo) {
        MerchantMeta storeMeta = merchantMetaService.getOne(
                new LambdaQueryWrapper<MerchantMeta>()
                        .eq(MerchantMeta::getMerNo, reqVo.getMerNo()));
        return Result.success(storeMeta);
    }


    @PostMapping("delete")
    public Result<Object> delete(@RequestBody MerchantMeta reqVo) {
        merchantMetaService.remove(
                new LambdaQueryWrapper<MerchantMeta>()
                        .eq(MerchantMeta::getMerNo, reqVo.getMerNo()));
        return Result.success();
    }
}
