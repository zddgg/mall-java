package com.zddgg.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.common.request.PaginationReq;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.SpuDetailReqVo;
import com.zddgg.mall.product.bean.SpuDetailRespVo;
import com.zddgg.mall.product.bean.SpuDetailUpdateReqVo;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.SkuAttrSaleMap;
import com.zddgg.mall.product.entity.SkuMeta;
import com.zddgg.mall.product.entity.SpuAttrSaleMap;
import com.zddgg.mall.product.entity.SpuMeta;
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

    @PostMapping("list")
    public Result<Page<SpuMeta>> list(@RequestBody PaginationReq req) {
        Page<SpuMeta> page = new Page<>(req.getCurrent(), req.getPageSize());
        LambdaQueryWrapper<SpuMeta> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(SpuMeta::getStatusFlag, StatusEnum.DELETED.code);
        spuMetaService.page(page, queryWrapper);
        return Result.success(page);
    }

    @PostMapping("detail")
    public Result<SpuDetailRespVo> detail(@RequestBody SpuDetailReqVo req) {
        SpuMeta spuMeta = spuMetaService.getOne(
                new LambdaQueryWrapper<SpuMeta>()
                        .eq(SpuMeta::getSpuId, req.getSpuId()));
        if (Objects.isNull(spuMeta)) {
            throw new BizException("spu信息查询失败！");
        }
        List<SpuAttrSaleMap> spuAttrSaleMaps = spuAttrSaleMapService.list(
                new LambdaQueryWrapper<SpuAttrSaleMap>()
                        .eq(SpuAttrSaleMap::getSpuId, req.getSpuId()));
        SpuDetailRespVo respVo = new SpuDetailRespVo();
        respVo.setSpuMeta(spuMeta);
        respVo.setSpuAttrSaleMaps(spuAttrSaleMaps);
        return Result.success(respVo);
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody SpuDetailUpdateReqVo req) {
        SpuMeta spuMeta = req.getSpuMeta();
        String spuId = idService.getId();
        spuMeta.setSpuId(spuId);
        spuMeta.setStatusFlag(StatusEnum.ENABLED.code);
        spuMetaService.save(spuMeta);

        List<String> addAttrNames = req.getAddAttrNames();
        if (!CollectionUtils.isEmpty(addAttrNames)) {
            List<SpuAttrSaleMap> saveList = addAttrNames
                    .stream().map(String::trim).collect(Collectors.toSet())
                    .stream().map(name -> {
                        SpuAttrSaleMap attrSaleMap = new SpuAttrSaleMap();
                        attrSaleMap.setSpuId(spuId);
                        attrSaleMap.setAttrId(idService.getId());
                        attrSaleMap.setAttrName(name);
                        attrSaleMap.setStatusFlag(StatusEnum.ENABLED.code);
                        return attrSaleMap;
                    }).collect(Collectors.toList());
            spuAttrSaleMapService.saveBatch(saveList);
        }
        return Result.success();
    }

    @PostMapping("update")
    public Result<Object> update(@RequestBody SpuDetailUpdateReqVo req) {
        SpuMeta spuMeta = req.getSpuMeta();
        SpuMeta one = spuMetaService.getOne(
                new LambdaQueryWrapper<SpuMeta>()
                        .eq(SpuMeta::getSpuId, spuMeta.getSpuId()));
        if (Objects.isNull(one)) {
            throw new BizException("SPU信息不存在！");
        }
        // todo 删除
        one.setStoreId(spuMeta.getStoreId());
        one.setSpuName(spuMeta.getSpuName());
        one.setBrandId(spuMeta.getBrandId());
        one.setCategoryId(spuMeta.getCategoryId());
        spuMetaService.updateById(one);
        List<String> addAttrNames = req.getAddAttrNames();
        if (!CollectionUtils.isEmpty(addAttrNames)) {
            SpuAttrSaleMap exist = spuAttrSaleMapService.getOne(
                    new LambdaQueryWrapper<SpuAttrSaleMap>()
                            .in(SpuAttrSaleMap::getAttrName, addAttrNames));
            if (Objects.nonNull(exist)) {
                throw new BizException("属性名" + exist.getAttrName() + "已存在！");
            }

            List<SpuAttrSaleMap> saveList = new ArrayList<>();
            addAttrNames.forEach(name -> {
                SpuAttrSaleMap attrSaleMap = new SpuAttrSaleMap();
                attrSaleMap.setSpuId(one.getSpuId());
                attrSaleMap.setAttrId(idService.getId());
                attrSaleMap.setAttrName(name);
                attrSaleMap.setStatusFlag(StatusEnum.ENABLED.code);
                saveList.add(attrSaleMap);
            });
            spuAttrSaleMapService.saveBatch(saveList);
        }
        List<String> deleteAttrIds = req.getDeleteAttrIds();
        if (!CollectionUtils.isEmpty(deleteAttrIds)) {
            SkuAttrSaleMap exist = skuAttrSaleMapService.getOne(
                    new LambdaQueryWrapper<SkuAttrSaleMap>()
                            .eq(SkuAttrSaleMap::getSpuId, one.getSpuId())
                            .in(SkuAttrSaleMap::getAttrId, deleteAttrIds));
            if (Objects.nonNull(exist)) {
                throw new BizException("已有SKU使用该属性，无法删除");
            }
            spuAttrSaleMapService.remove(
                    new LambdaQueryWrapper<SpuAttrSaleMap>()
                            .eq(SpuAttrSaleMap::getSpuId, one.getSpuId())
                            .in(SpuAttrSaleMap::getAttrId, deleteAttrIds));
        }
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
        spuAttrSaleMapService.remove(
                new LambdaQueryWrapper<SpuAttrSaleMap>()
                        .eq(SpuAttrSaleMap::getSpuId, req.getSpuId()));
        return Result.success();
    }
}
