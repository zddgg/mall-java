package com.zddgg.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.common.request.PaginationReq;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.SpuAttrReqVo;
import com.zddgg.mall.product.bean.SpuCreateReqVo;
import com.zddgg.mall.product.bean.SpuDetailReqVo;
import com.zddgg.mall.product.bean.SpuSkuCreateReqVo;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.*;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("product/spu")
@RequiredArgsConstructor
public class SpuController {

    private final IdService idService;

    private final SpuMetaService spuMetaService;

    private final SkuMetaService skuMetaService;

    private final SpuAttrSaleMapService spuAttrSaleMapService;

    private final SkuAttrSaleMapService skuAttrSaleMapService;

    private final AttrSaleKeyService attrSaleKeyService;

    private final AttrSaleValueService attrSaleValueService;

    @PostMapping("list")
    public Result<Page<SpuMeta>> list(@RequestBody PaginationReq req) {
        Page<SpuMeta> page = new Page<>(req.getCurrent(), req.getPageSize());
        LambdaQueryWrapper<SpuMeta> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(SpuMeta::getStatusFlag, StatusEnum.DELETED.code);
        spuMetaService.page(page, queryWrapper);
        return Result.success(page);
    }

    @PostMapping("detail")
    public Result<SpuMeta> detail(@RequestBody SpuDetailReqVo req) {
        SpuMeta spuMeta = spuMetaService.getOne(
                new LambdaQueryWrapper<SpuMeta>()
                        .eq(SpuMeta::getSpuId, req.getSpuId()));
        return Result.success(spuMeta);
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody SpuCreateReqVo req) {
        SpuMeta spuMeta = new SpuMeta();
        spuMeta.setSpuId(idService.getId());
        spuMeta.setSpuName(req.getSpuName());
        spuMeta.setStoreId(req.getStoreId());
        spuMeta.setBrandId(req.getBrandId());
        spuMeta.setCategoryId(req.getCategoryId());
        spuMeta.setStatusFlag(StatusEnum.ENABLED.code);
        spuMetaService.save(spuMeta);
        return Result.success();
    }

    @PostMapping("update")
    public Result<Object> update(@RequestBody SpuCreateReqVo req) {
        SpuMeta one = spuMetaService.getOne(
                new LambdaQueryWrapper<SpuMeta>()
                        .eq(SpuMeta::getSpuId, req.getSpuId()));
        if (Objects.isNull(one)) {
            throw new BizException("SPU信息不存在！");
        }
        // todo 删除
        one.setSpuName(req.getSpuName());
        one.setStoreId(req.getStoreId());
        one.setBrandId(req.getBrandId());
        one.setCategoryId(req.getCategoryId());
        spuMetaService.updateById(one);
        return Result.success();
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody SpuDetailReqVo req) {
        SkuMeta skuMeta = skuMetaService.getOne(
                new LambdaQueryWrapper<SkuMeta>()
                        .eq(SkuMeta::getSpuId, req.getSpuId()));
        if (Objects.nonNull(skuMeta)) {
            throw new BizException("存在商品, 商品编号：" + skuMeta.getSkuId() + ", 删除失败");
        }
        spuMetaService.remove(
                new LambdaQueryWrapper<SpuMeta>()
                        .eq(SpuMeta::getSpuId, req.getSpuId()));
        return Result.success();
    }

    @PostMapping("queryAttrList")
    public Result<List<AttrSaleKey>> queryAttrList(@RequestBody SpuAttrReqVo req) {
        List<SpuAttrSale> saleMaps = spuAttrSaleMapService.list(
                new LambdaQueryWrapper<SpuAttrSale>()
                        .eq(SpuAttrSale::getSpuId, req.getSpuId()));
        List<AttrSaleKey> saleKeys = new ArrayList<>();
        if (!CollectionUtils.isEmpty(saleMaps)) {
            List<String> attrIds = saleMaps.stream().map(SpuAttrSale::getAttrId).collect(Collectors.toList());
            saleKeys = attrSaleKeyService.list(
                    new LambdaQueryWrapper<AttrSaleKey>()
                            .in(AttrSaleKey::getAttrId, attrIds));
            Map<String, List<AttrSaleValue>> saleValueMap = attrSaleValueService.list(
                            new LambdaQueryWrapper<AttrSaleValue>()
                                    .in(AttrSaleValue::getAttrId, attrIds))
                    .stream().collect(Collectors.groupingBy(AttrSaleValue::getAttrId));
            saleKeys.forEach((item) -> item.setAttrSaleValues(saleValueMap.get(item.getAttrId())));
        }
        return Result.success(saleKeys);
    }

    @PostMapping("addAttrSale")
    public Result<Object> addAttrSale(@RequestBody SpuAttrReqVo req) {
        SpuMeta spuMeta = spuMetaService.getOne(
                new LambdaQueryWrapper<SpuMeta>()
                        .eq(SpuMeta::getSpuId, req.getSpuId()));
        if (Objects.isNull(spuMeta)) {
            throw new BizException("SPU信息不存在！");
        }
        AttrSaleKey attrSaleKey = attrSaleKeyService.getOne(
                new LambdaQueryWrapper<AttrSaleKey>()
                        .eq(AttrSaleKey::getAttrId, req.getAttrId()));
        if (Objects.isNull(attrSaleKey)) {
            throw new BizException("销售属性信息不存在！");
        }
        SpuAttrSale attrSaleMap = spuAttrSaleMapService.getOne(
                new LambdaQueryWrapper<SpuAttrSale>()
                        .eq(SpuAttrSale::getSpuId, req.getSpuId())
                        .eq(SpuAttrSale::getAttrId, req.getAttrId()));
        if (Objects.nonNull(attrSaleMap)) {
            throw new BizException("销售属性已绑定！");
        }
        SpuAttrSale save = new SpuAttrSale();
        save.setSpuId(req.getSpuId());
        save.setAttrId(req.getAttrId());
        save.setAttrName(attrSaleKey.getAttrName());
        save.setStatusFlag(StatusEnum.ENABLED.code);
        spuAttrSaleMapService.save(save);
        return Result.success();
    }

    @PostMapping("deleteAttrSale")
    public Result<Object> deleteAttrSale(@RequestBody SpuAttrReqVo req) {
        SkuAttrSale skuAttrSale = skuAttrSaleMapService.getOne(
                new LambdaQueryWrapper<SkuAttrSale>()
                        .eq(SkuAttrSale::getSpuId, req.getSpuId())
                        .eq(SkuAttrSale::getAttrId, req.getAttrId()));
        if (Objects.nonNull(skuAttrSale)) {
            throw new BizException("已有SKU绑定该属性，无法删除！");
        }
        spuAttrSaleMapService.remove(
                new LambdaQueryWrapper<SpuAttrSale>()
                        .eq(SpuAttrSale::getSpuId, req.getSpuId())
                        .eq(SpuAttrSale::getAttrId, req.getAttrId()));
        return Result.success();
    }

    @PostMapping("spuSkuCreate")
    public Result<Object> deleteAttrSale(@RequestBody SpuSkuCreateReqVo req) {
        SpuMeta spuMeta = new SpuMeta();
        String spuId = idService.getId();
        spuMeta.setSpuId(spuId);
        spuMeta.setSpuName(req.getSpuName());
        spuMeta.setStoreId(req.getStoreId());
        spuMeta.setBrandId(req.getBrandId());
        spuMeta.setCategoryId(req.getCategoryId());
        spuMeta.setStatusFlag(StatusEnum.ENABLED.code);
        spuMetaService.save(spuMeta);

        List<SpuAttrSale> spuAttrSales = req.getSpuAttrSaleDataList().stream()
                .map((item) -> {
                    SpuAttrSale spuAttrSale = new SpuAttrSale();
                    spuAttrSale.setSpuId(spuId);
                    spuAttrSale.setAttrId(item.getKeyId());
                    spuAttrSale.setAttrName(item.getKeyName());
                    spuAttrSale.setStatusFlag(StatusEnum.ENABLED.code);
                    return spuAttrSale;
                })
                .collect(Collectors.toList());
        spuAttrSaleMapService.saveBatch(spuAttrSales);

        List<SkuMeta> skuSaveList = new ArrayList<>();
        List<SkuAttrSale> skuAttrSaleSaveList = new ArrayList<>();
        for (SpuSkuCreateReqVo.SkuItem skuItem : req.getSkuList()) {
            String skuId = idService.getId();
            SkuMeta skuMeta = new SkuMeta();
            skuMeta.setSpuId(spuId);
            skuMeta.setSkuId(skuId);
            skuMeta.setSkuName(skuItem.getSkuName());
            skuMeta.setRetailPrice(new BigDecimal(skuItem.getRetailPrice()));
            skuMeta.setStatusFlag(StatusEnum.ENABLED.code);
            List<SkuAttrSale> skuAttrSales = skuItem.getAttrList().stream()
                    .map((item) -> {
                        SkuAttrSale skuAttrSale = new SkuAttrSale();
                        skuAttrSale.setSpuId(spuId);
                        skuAttrSale.setSkuId(skuId);
                        skuAttrSale.setAttrId(item.getAttrId());
                        skuAttrSale.setAttrValueId(item.getAttrValueId());
                        skuAttrSale.setAttrValueName(item.getAttrValueName());
                        skuAttrSale.setStatusFlag(StatusEnum.ENABLED.code);
                        return skuAttrSale;
                    }).collect(Collectors.toList());
            skuSaveList.add(skuMeta);
            skuAttrSaleSaveList.addAll(skuAttrSales);
        }
        skuMetaService.saveBatch(skuSaveList);
        skuAttrSaleMapService.saveBatch(skuAttrSaleSaveList);
        return Result.success();
    }
}
