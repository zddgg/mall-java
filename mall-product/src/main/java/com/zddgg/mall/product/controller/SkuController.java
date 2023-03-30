package com.zddgg.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.common.request.PaginationReq;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.SkuDetailReqVo;
import com.zddgg.mall.product.bean.SpuCreateReqVo;
import com.zddgg.mall.product.bean.sku.SkuDeleteReqVo;
import com.zddgg.mall.product.bean.sku.SkuUpdateReqVo;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.SkuAttrSale;
import com.zddgg.mall.product.entity.SkuMeta;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.SkuAttrSaleMapService;
import com.zddgg.mall.product.service.SkuMetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping("product/sku")
@RequiredArgsConstructor
public class SkuController {

    private final SkuMetaService skuMetaService;

    private final SkuAttrSaleMapService skuAttrSaleMapService;

    @PostMapping("list")
    public Result<Page<SkuMeta>> list(@RequestBody PaginationReq req) {
        Page<SkuMeta> page = new Page<>(req.getCurrent(), req.getPageSize());
        LambdaQueryWrapper<SkuMeta> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(SkuMeta::getStatusFlag, StatusEnum.DELETED.code);
        skuMetaService.page(page, queryWrapper);
        return Result.success(page);
    }

    @PostMapping("detail")
    public Result<SkuMeta> detail(@RequestBody SkuDetailReqVo req) {
        SkuMeta skuMeta = skuMetaService.getOne(
                new LambdaQueryWrapper<SkuMeta>()
                        .eq(SkuMeta::getSkuId, req.getSkuId()));
        return Result.success(skuMeta);
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody SpuCreateReqVo req) {
        return Result.success();
    }

    @PostMapping("update")
    public Result<Object> update(@RequestBody SkuUpdateReqVo req) {
        System.out.println(req);
        SkuMeta skuMeta = skuMetaService.getOne(
                new LambdaQueryWrapper<SkuMeta>()
                        .eq(SkuMeta::getSkuId, req.getSkuId())
        );
        if (Objects.isNull(skuMeta)) {
            throw new BizException("sku信息不存在！");
        }
        skuMeta.setSkuName(req.getSkuName());
        skuMeta.setRetailPrice(BigDecimal.valueOf(req.getRetailPrice()));
        skuMetaService.updateById(skuMeta);
        return Result.success();
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody SkuDeleteReqVo req) {
        skuMetaService.remove(
                new LambdaQueryWrapper<SkuMeta>()
                        .eq(SkuMeta::getSkuId, req.getSkuId())
        );
        skuAttrSaleMapService.remove(
                new LambdaQueryWrapper<SkuAttrSale>()
                        .eq(SkuAttrSale::getSkuId, req.getSkuId())
        );
        return Result.success();
    }
}
