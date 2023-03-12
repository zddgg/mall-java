package com.zddgg.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.*;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.CategoryPropertySale;
import com.zddgg.mall.product.entity.PropertySaleKey;
import com.zddgg.mall.product.entity.PropertySaleValue;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.CategoryPropertySaleService;
import com.zddgg.mall.product.service.IdService;
import com.zddgg.mall.product.service.PropertySaleKeyService;
import com.zddgg.mall.product.service.PropertySaleValueService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("property/sale")
@RequiredArgsConstructor
public class PropertySaleController {

    private final PropertySaleKeyService propertySaleKeyService;

    private final PropertySaleValueService propertySaleValueService;

    private final CategoryPropertySaleService categoryPropertySaleService;

    private final IdService idService;

    @PostMapping("list")
    public Result<Page<PropertySaleKey>> list(@RequestBody PropertySaleQueryReqVo req) {
        LambdaQueryWrapper<PropertySaleKey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNoneBlank(req.getKeyId()),
                PropertySaleKey::getKeyId, req.getKeyId());
        queryWrapper.like(StringUtils.isNoneBlank(req.getKeyName()),
                PropertySaleKey::getKeyName, req.getKeyName());
        Page<PropertySaleKey> page = new Page<>(req.getCurrent(), req.getPageSize());
        propertySaleKeyService.page(page, queryWrapper);
        List<String> keyIds = page.getRecords().stream().map(PropertySaleKey::getKeyId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(keyIds)) {
            Map<String, List<PropertySaleValue>> saleValueMap = propertySaleValueService.list(
                            new LambdaQueryWrapper<PropertySaleValue>()
                                    .in(PropertySaleValue::getKeyId, keyIds))
                    .stream().collect(Collectors.groupingBy(PropertySaleValue::getKeyId));
            page.getRecords().forEach((item) -> item.setPropertySaleValues(saleValueMap.get(item.getKeyId())));
        }
        return Result.success(page);
    }

    @PostMapping("queryByCategoryId")
    public Result<List<PropertySaleKey>> queryByCategoryId(@RequestBody PropertySaleQueryByCategoryIdReqVo req) {
        List<CategoryPropertySale> propertySales = categoryPropertySaleService.list(
                new LambdaQueryWrapper<CategoryPropertySale>()
                        .eq(CategoryPropertySale::getCategoryId, req.getCategoryId()));
        List<String> attrSaleIds = propertySales.stream().map(CategoryPropertySale::getPropertySaleId)
                .collect(Collectors.toList());
        List<PropertySaleKey> propertySaleKeys = propertySaleKeyService.list(
                new LambdaQueryWrapper<PropertySaleKey>()
                        .in(PropertySaleKey::getKeyId, attrSaleIds));
        Map<String, List<PropertySaleValue>> saleValueMap = propertySaleValueService.list(
                        new LambdaQueryWrapper<PropertySaleValue>()
                                .in(PropertySaleValue::getKeyId, attrSaleIds))
                .stream().collect(Collectors.groupingBy(PropertySaleValue::getKeyId));
        propertySaleKeys.forEach((item) -> item.setPropertySaleValues(saleValueMap.get(item.getKeyId())));
        return Result.success(propertySaleKeys);
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody @Validated PropertySaleCreateReqVo vo) {
        LambdaQueryWrapper<PropertySaleKey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PropertySaleKey::getKeyName, vo.getKeyName());
        PropertySaleKey one = propertySaleKeyService.getOne(queryWrapper);
        if (Objects.nonNull(one)) {
            throw new BizException("销售属性名称[" + vo.getKeyName() + "]已录入，请检查属性名称！");
        }
        PropertySaleKey saveSaleKey = new PropertySaleKey();
        saveSaleKey.setKeyId(idService.getId());
        saveSaleKey.setKeyName(vo.getKeyName());
        saveSaleKey.setStatus(StatusEnum.ENABLED.code);
        propertySaleKeyService.save(saveSaleKey);
        return Result.success();
    }

    @PostMapping("detail")
    public Result<PropertySaleKey> detail(@RequestBody @Validated PropertySaleQueryReqVo req) {
        PropertySaleKey saleKey = propertySaleKeyService.getOne(
                new LambdaQueryWrapper<PropertySaleKey>()
                        .eq(PropertySaleKey::getKeyId, req.getKeyId()));
        if (Objects.nonNull(saleKey)) {
            List<PropertySaleValue> saleValues = propertySaleValueService.list(
                    new LambdaQueryWrapper<PropertySaleValue>()
                            .eq(PropertySaleValue::getKeyId, req.getKeyId()));
            saleKey.setPropertySaleValues(saleValues);
        }
        return Result.success(saleKey);
    }

    @PostMapping("key/edit")
    public Result<Object> edit(@RequestBody @Validated PropertySaleDetailReqVo vo) {
        PropertySaleKey one = propertySaleKeyService.getOne(
                new LambdaQueryWrapper<PropertySaleKey>()
                        .eq(PropertySaleKey::getKeyId, vo.getKeyId()));
        if (Objects.isNull(one)) {
            throw new BizException("销售属性不存在，请刷新页面！");
        }
        one.setKeyName(vo.getKeyName());
        propertySaleKeyService.updateById(one);
        return Result.success();
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody @Validated PropertySaleQueryReqVo reqVo) {
        propertySaleKeyService.remove(
                new LambdaQueryWrapper<PropertySaleKey>()
                        .eq(PropertySaleKey::getKeyId, reqVo.getKeyId()));
        propertySaleValueService.remove(
                new LambdaQueryWrapper<PropertySaleValue>()
                        .eq(PropertySaleValue::getKeyId, reqVo.getKeyId()));
        return Result.success();
    }

    @PostMapping("addValue")
    public Result<Object> addValue(@RequestBody @Validated PropertySaleAddValueReqVo reqVo) {
        PropertySaleKey propertySaleKey = propertySaleKeyService.getOne(
                new LambdaQueryWrapper<PropertySaleKey>()
                        .eq(PropertySaleKey::getKeyId, reqVo.getKeyId()));
        if (Objects.isNull(propertySaleKey)) {
            throw new BizException("销售属性不存在！");
        }
        PropertySaleValue saleValue = propertySaleValueService.getOne(
                new LambdaQueryWrapper<PropertySaleValue>()
                        .eq(PropertySaleValue::getKeyId, reqVo.getKeyId())
                        .eq(PropertySaleValue::getValueName, reqVo.getValueName()));
        if (Objects.nonNull(saleValue)) {
            throw new BizException("属性值名称已存在！");
        }
        PropertySaleValue save = new PropertySaleValue();
        save.setKeyId(reqVo.getKeyId());
        save.setValueId(idService.getId());
        save.setValueName(reqVo.getValueName());
        save.setPropertyOrder(0);
        propertySaleValueService.save(save);
        return Result.success();
    }

    @PostMapping("deleteValue")
    public Result<Object> deleteValue(@RequestBody @Validated PropertySaleDeleteReqVo reqVo) {
        propertySaleValueService.remove(
                new LambdaQueryWrapper<PropertySaleValue>()
                        .eq(PropertySaleValue::getKeyId, reqVo.getKeyId())
                        .eq(PropertySaleValue::getValueId, reqVo.getValueId()));
        return Result.success();
    }

    @PostMapping("key/detail")
    public Result<PropertySaleKey> queryKeyDetail(@RequestBody @Validated PropertySaleQueryReqVo reqVo) {
        PropertySaleKey saleKey = propertySaleKeyService.getOne(
                new LambdaQueryWrapper<PropertySaleKey>()
                        .eq(PropertySaleKey::getKeyId, reqVo.getKeyId()));
        return Result.success(saleKey);
    }

    @PostMapping("value/list")
    public Result<List<PropertySaleValue>> queryValueList(@RequestBody @Validated PropertySaleQueryReqVo reqVo) {
        List<PropertySaleValue> saleValues = propertySaleValueService.list(
                new LambdaQueryWrapper<PropertySaleValue>()
                        .eq(PropertySaleValue::getKeyId, reqVo.getKeyId()));
        return Result.success(saleValues);
    }
}
