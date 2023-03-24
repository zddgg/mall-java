package com.zddgg.mall.product.controller;

import com.alibaba.fastjson2.JSON;
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

    private final PropertySaleKeyService propertySaleKeyService;

    private final PropertySaleValueService propertySaleValueService;

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
    public Result<List<PropertySaleKey>> queryAttrList(@RequestBody SpuAttrReqVo req) {
        List<SpuAttrSaleMap> saleMaps = spuAttrSaleMapService.list(
                new LambdaQueryWrapper<SpuAttrSaleMap>()
                        .eq(SpuAttrSaleMap::getSpuId, req.getSpuId()));
        List<PropertySaleKey> saleKeys = new ArrayList<>();
        if (!CollectionUtils.isEmpty(saleMaps)) {
            List<String> attrIds = saleMaps.stream().map(SpuAttrSaleMap::getAttrId).collect(Collectors.toList());
            saleKeys = propertySaleKeyService.list(
                    new LambdaQueryWrapper<PropertySaleKey>()
                            .in(PropertySaleKey::getKeyId, attrIds));
            Map<String, List<PropertySaleValue>> saleValueMap = propertySaleValueService.list(
                            new LambdaQueryWrapper<PropertySaleValue>()
                                    .in(PropertySaleValue::getKeyId, attrIds))
                    .stream().collect(Collectors.groupingBy(PropertySaleValue::getKeyId));
            saleKeys.forEach((item) -> item.setPropertySaleValues(saleValueMap.get(item.getKeyId())));
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
        PropertySaleKey propertySaleKey = propertySaleKeyService.getOne(
                new LambdaQueryWrapper<PropertySaleKey>()
                        .eq(PropertySaleKey::getKeyId, req.getAttrId()));
        if (Objects.isNull(propertySaleKey)) {
            throw new BizException("销售属性信息不存在！");
        }
        SpuAttrSaleMap attrSaleMap = spuAttrSaleMapService.getOne(
                new LambdaQueryWrapper<SpuAttrSaleMap>()
                        .eq(SpuAttrSaleMap::getSpuId, req.getSpuId())
                        .eq(SpuAttrSaleMap::getAttrId, req.getAttrId()));
        if (Objects.nonNull(attrSaleMap)) {
            throw new BizException("销售属性已绑定！");
        }
        SpuAttrSaleMap save = new SpuAttrSaleMap();
        save.setSpuId(req.getSpuId());
        save.setAttrId(req.getAttrId());
        save.setAttrName(propertySaleKey.getKeyName());
        save.setStatusFlag(StatusEnum.ENABLED.code);
        spuAttrSaleMapService.save(save);
        return Result.success();
    }

    @PostMapping("deleteAttrSale")
    public Result<Object> deleteAttrSale(@RequestBody SpuAttrReqVo req) {
        SkuAttrSaleMap skuAttrSaleMap = skuAttrSaleMapService.getOne(
                new LambdaQueryWrapper<SkuAttrSaleMap>()
                        .eq(SkuAttrSaleMap::getSpuId, req.getSpuId())
                        .eq(SkuAttrSaleMap::getAttrId, req.getAttrId()));
        if (Objects.nonNull(skuAttrSaleMap)) {
            throw new BizException("已有SKU绑定该属性，无法删除！");
        }
        spuAttrSaleMapService.remove(
                new LambdaQueryWrapper<SpuAttrSaleMap>()
                        .eq(SpuAttrSaleMap::getSpuId, req.getSpuId())
                        .eq(SpuAttrSaleMap::getAttrId, req.getAttrId()));
        return Result.success();
    }

    @PostMapping("spuSkuCreate")
    public Result<Object> deleteAttrSale(@RequestBody SpuSkuCreateReqVo req) {
        System.out.println(JSON.toJSONString(req));
        return Result.success();
    }
}
