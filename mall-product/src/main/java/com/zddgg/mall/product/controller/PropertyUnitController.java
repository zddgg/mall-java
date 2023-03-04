package com.zddgg.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.voidtime.mall.common.response.Result;
import com.voidtime.mall.product.bean.PropertyUnitCreateReqVo;
import com.voidtime.mall.product.bean.PropertyUnitDeleteReqVo;
import com.voidtime.mall.product.bean.PropertyUnitQueryReqVo;
import com.voidtime.mall.product.biz.service.PropertyUnitBizService;
import com.voidtime.mall.product.entity.PropertyUnitKey;
import com.voidtime.mall.product.service.PropertyUnitKeyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("property/unit")
public class PropertyUnitController {

    private final PropertyUnitKeyService propertyUnitKeyService;

    private final PropertyUnitBizService propertyUnitBizService;

    public PropertyUnitController(PropertyUnitKeyService propertyUnitKeyService,
                                  PropertyUnitBizService propertyUnitBizService) {
        this.propertyUnitKeyService = propertyUnitKeyService;
        this.propertyUnitBizService = propertyUnitBizService;
    }

    @PostMapping("list")
    public Result<Page<PropertyUnitKey>> list(@RequestBody PropertyUnitQueryReqVo req) {
        LambdaQueryWrapper<PropertyUnitKey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNoneBlank(req.getUnitKeyName()),
                PropertyUnitKey::getUnitKeyName, req.getUnitKeyName());
        Page<PropertyUnitKey> page = new Page<>(req.getCurrent(), req.getPageSize());
        propertyUnitKeyService.page(page, queryWrapper);
        List<PropertyUnitKey> propertyUnitKeys = propertyUnitBizService.getListAndRelatedByPropertyNos(
                page.getRecords().stream()
                        .map(PropertyUnitKey::getUnitKeyId).collect(Collectors.toList()));
        page.setRecords(propertyUnitKeys);
        return Result.success(page);
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody @Validated PropertyUnitCreateReqVo vo) {
        propertyUnitBizService.saveKeyAndValue(vo);
        return Result.success();
    }

    @PostMapping("detail")
    public Result<PropertyUnitKey> detail(@RequestBody @Validated PropertyUnitKey req) {
        PropertyUnitKey propertyUnitKey = propertyUnitBizService
                .getDetailAndRelatedByPropertyNo(req.getUnitKeyId());
        return Result.success(propertyUnitKey);
    }

    @PostMapping("edit")
    public Result<Object> edit(@RequestBody @Validated PropertyUnitCreateReqVo vo) {
        propertyUnitBizService.edit(vo);
        return Result.success();
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody @Validated PropertyUnitDeleteReqVo reqVo) {
        propertyUnitBizService.delete(reqVo);
        return Result.success();
    }
}
