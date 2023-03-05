package com.zddgg.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.PropertySaleCreateReqVo;
import com.zddgg.mall.product.bean.PropertySaleDetailReqVo;
import com.zddgg.mall.product.bean.PropertySaleQueryReqVo;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.PropertySaleKey;
import com.zddgg.mall.product.entity.PropertySaleValue;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.IdService;
import com.zddgg.mall.product.service.PropertySaleKeyService;
import com.zddgg.mall.product.service.PropertySaleValueService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("property/sale")
@RequiredArgsConstructor
public class PropertySaleController {

    private final PropertySaleKeyService propertySaleKeyService;

    private final PropertySaleValueService propertySaleValueService;

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
        Map<String, List<PropertySaleValue>> saleValueMap = propertySaleValueService.list(
                        new LambdaQueryWrapper<PropertySaleValue>()
                                .in(PropertySaleValue::getSaleKeyId, keyIds))
                .stream().collect(Collectors.groupingBy(PropertySaleValue::getSaleKeyId));
        page.getRecords().forEach((item) -> item.setPropertySaleValues(saleValueMap.get(item.getKeyId())));
        return Result.success(page);
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
        saveSaleKey.setStatus(StatusEnum.DISABLED.code);
        propertySaleKeyService.save(saveSaleKey);

        ArrayList<PropertySaleValue> valueSaveList = new ArrayList<>();
        for (PropertySaleCreateReqVo.PropertySaleValue propertySaleValue : vo.getPropertySaleValues()) {
            PropertySaleValue value = new PropertySaleValue();
            value.setSaleKeyId(saveSaleKey.getKeyId());
            value.setPropertyValue(propertySaleValue.getPropertyValue());
            value.setPropertyOrder(propertySaleValue.getPropertyOrder());
            valueSaveList.add(value);
        }
        propertySaleValueService.saveBatch(valueSaveList);
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
                            .eq(PropertySaleValue::getSaleKeyId, req.getKeyId()));
            saleKey.setPropertySaleValues(saleValues);
        }
        return Result.success(saleKey);
    }

    @PostMapping("edit")
    public Result<Object> edit(@RequestBody @Validated PropertySaleDetailReqVo vo) {
        PropertySaleKey one = propertySaleKeyService.getOne(
                new LambdaQueryWrapper<PropertySaleKey>()
                        .eq(PropertySaleKey::getKeyId, vo.getKeyId()));
        if (Objects.isNull(one)) {
            throw new BizException("销售属性不存在，请刷新列表！");
        }
        one.setKeyName(vo.getKeyName());
        propertySaleKeyService.updateById(one);

        propertySaleValueService.remove(
                new LambdaQueryWrapper<PropertySaleValue>()
                        .eq(PropertySaleValue::getSaleKeyId, one.getKeyId()));

        ArrayList<PropertySaleValue> valueSaveList = new ArrayList<>();
        for (PropertySaleDetailReqVo.PropertySaleValue saleValue : vo.getPropertySaleValues()) {
            PropertySaleValue value = new PropertySaleValue();
            value.setSaleKeyId(one.getKeyId());
            value.setPropertyValue(saleValue.getPropertyValue());
            value.setPropertyOrder(saleValue.getPropertyOrder());
            valueSaveList.add(value);
        }
        propertySaleValueService.saveBatch(valueSaveList);
        return Result.success();
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody @Validated PropertySaleQueryReqVo reqVo) {
        propertySaleKeyService.remove(
                new LambdaQueryWrapper<PropertySaleKey>()
                        .eq(PropertySaleKey::getKeyId, reqVo.getKeyId()));
        propertySaleValueService.remove(
                new LambdaQueryWrapper<PropertySaleValue>()
                        .eq(PropertySaleValue::getSaleKeyId, reqVo.getKeyId()));
        return Result.success();
    }
}
