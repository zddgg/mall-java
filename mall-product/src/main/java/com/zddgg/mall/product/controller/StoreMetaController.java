package com.zddgg.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.common.request.PaginationReq;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.MerchantMeta;
import com.zddgg.mall.product.entity.StoreMeta;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.IdService;
import com.zddgg.mall.product.service.MerchantMetaService;
import com.zddgg.mall.product.service.StoreMetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("store")
@RequiredArgsConstructor
public class StoreMetaController {

    private final IdService idService;

    private final StoreMetaService storeMetaService;

    private final MerchantMetaService merchantMetaService;

    @PostMapping("list")
    public Result<Page<StoreMeta>> list(@RequestBody PaginationReq req) {
        Page<StoreMeta> page = new Page<>(req.getCurrent(), req.getPageSize());
        LambdaQueryWrapper<StoreMeta> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(StoreMeta::getStatusFlag, StatusEnum.DELETED.code);
        storeMetaService.page(page, queryWrapper);
        return Result.success(page);
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody StoreMeta reqVo) {
        MerchantMeta merCheck = merchantMetaService.getOne(
                new LambdaQueryWrapper<MerchantMeta>()
                        .eq(MerchantMeta::getMerNo, reqVo.getMerNo()));
        if (Objects.isNull(merCheck)) {
            throw new BizException("商户不存在！");
        }
        StoreMeta nameCheck = storeMetaService.getOne(
                new LambdaQueryWrapper<StoreMeta>()
                        .eq(StoreMeta::getStoreName, reqVo.getStoreName()));
        if (Objects.nonNull(nameCheck)) {
            throw new BizException("店铺名称已不存在！");
        }
        StoreMeta storeMeta = new StoreMeta();
        storeMeta.setMerNo(reqVo.getMerNo());
        storeMeta.setStoreNo(idService.getId());
        storeMeta.setStoreName(reqVo.getStoreName());
        storeMeta.setStatusFlag(StatusEnum.ENABLED.code);
        storeMetaService.save(storeMeta);
        return Result.success();
    }

    @PostMapping("update")
    public Result<Object> update(@RequestBody StoreMeta reqVo) {
        StoreMeta noCheck = storeMetaService.getOne(
                new LambdaQueryWrapper<StoreMeta>()
                        .eq(StoreMeta::getStoreNo, reqVo.getStoreNo()));
        if (Objects.isNull(noCheck)) {
            throw new BizException("店铺不存在！");
        }
        noCheck.setStoreName(reqVo.getStoreName());
        storeMetaService.updateById(noCheck);
        return Result.success();
    }

    @PostMapping("detail")
    public Result<StoreMeta> detail(@RequestBody StoreMeta reqVo) {
        StoreMeta storeMeta = storeMetaService.getOne(
                new LambdaQueryWrapper<StoreMeta>()
                        .eq(StoreMeta::getStoreNo, reqVo.getStoreNo()));
        return Result.success(storeMeta);
    }


    @PostMapping("delete")
    public Result<Object> delete(@RequestBody StoreMeta reqVo) {
        storeMetaService.remove(
                new LambdaQueryWrapper<StoreMeta>()
                        .eq(StoreMeta::getStoreNo, reqVo.getStoreNo()));
        return Result.success();
    }
}
