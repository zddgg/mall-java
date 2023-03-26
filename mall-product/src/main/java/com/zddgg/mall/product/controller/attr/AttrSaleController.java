package com.zddgg.mall.product.controller.attr;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.PropertySaleQueryByCategoryIdReqVo;
import com.zddgg.mall.product.bean.attr.req.*;
import com.zddgg.mall.product.bean.attr.resp.AttrSaleRecordRespVo;
import com.zddgg.mall.product.biz.AttrSaleBizService;
import com.zddgg.mall.product.entity.AttrSaleKey;
import com.zddgg.mall.product.entity.AttrSaleValue;
import com.zddgg.mall.product.entity.CategoryPropertySale;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.AttrSaleKeyService;
import com.zddgg.mall.product.service.AttrSaleValueService;
import com.zddgg.mall.product.service.CategoryPropertySaleService;
import com.zddgg.mall.product.service.IdService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("attr/sale")
@RequiredArgsConstructor
public class AttrSaleController {

    private final AttrSaleKeyService attrSaleKeyService;

    private final AttrSaleValueService attrSaleValueService;

    private final CategoryPropertySaleService categoryPropertySaleService;

    private final IdService idService;

    private final AttrSaleBizService attrSaleBizService;

    @PostMapping("page")
    public Result<Page<AttrSaleRecordRespVo>> page(@RequestBody AttrSaleRecordPageReqVo req) {
        return Result.success(attrSaleBizService.getAttrSaleRecordPage(req));
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody @Validated AttrSaleCreateReqVo req) {
        attrSaleBizService.create(req);
        return Result.success();
    }

    @PostMapping("detail")
    public Result<AttrSaleRecordRespVo> detail(@RequestBody @Validated AttrSaleDetailReqVo req) {
        return Result.success(attrSaleBizService.detail(req));
    }

    @PostMapping("key/update")
    public Result<Object> keyUpdate(@RequestBody @Validated AttrSaleDetailReqVo req) {
        AttrSaleKey one = attrSaleKeyService.getOne(
                new LambdaQueryWrapper<AttrSaleKey>()
                        .eq(AttrSaleKey::getAttrId, req.getAttrId()));
        if (Objects.isNull(one)) {
            throw new BizException("销售属性不存在，请刷新页面！");
        }
        one.setAttrName(req.getAttrName());
        attrSaleKeyService.updateById(one);
        return Result.success();
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody @Validated AttrSaleDeleteReqVo reqVo) {
        attrSaleKeyService.remove(
                new LambdaQueryWrapper<AttrSaleKey>()
                        .eq(AttrSaleKey::getAttrId, reqVo.getAttrId()));
        attrSaleValueService.remove(
                new LambdaQueryWrapper<AttrSaleValue>()
                        .eq(AttrSaleValue::getAttrId, reqVo.getAttrId()));
        return Result.success();
    }

    @PostMapping("addValue")
    public Result<Object> addValue(@RequestBody @Validated AttrSaleAddValueReqVo reqVo) {
        AttrSaleKey attrSaleKey = attrSaleKeyService.getOne(
                new LambdaQueryWrapper<AttrSaleKey>()
                        .eq(AttrSaleKey::getAttrId, reqVo.getAttrId()));
        if (Objects.isNull(attrSaleKey)) {
            throw new BizException("销售属性不存在！");
        }
        AttrSaleValue saleValue = attrSaleValueService.getOne(
                new LambdaQueryWrapper<AttrSaleValue>()
                        .eq(AttrSaleValue::getAttrId, reqVo.getAttrId())
                        .eq(AttrSaleValue::getAttrValueName, reqVo.getAttrValueName()));
        if (Objects.nonNull(saleValue)) {
            throw new BizException("属性值名称已存在！");
        }
        AttrSaleValue save = new AttrSaleValue();
        save.setAttrId(attrSaleKey.getAttrId());
        save.setAttrValueId(idService.getId());
        save.setAttrValueName(reqVo.getAttrValueName());
        save.setAttrValueOrder(0);
        attrSaleValueService.save(save);
        return Result.success();
    }

    @PostMapping("deleteValue")
    public Result<Object> deleteValue(@RequestBody @Validated AttrSaleValueDeleteReqVo reqVo) {
        attrSaleValueService.remove(
                new LambdaQueryWrapper<AttrSaleValue>()
                        .eq(AttrSaleValue::getAttrId, reqVo.getAttrId())
                        .eq(AttrSaleValue::getAttrValueId, reqVo.getAttrValueId()));
        return Result.success();
    }

    @PostMapping("value/list")
    public Result<List<AttrSaleRecordRespVo.AttrSaleValueVo>> queryValueList(@RequestBody @Validated AttrSaleDetailReqVo reqVo) {
        List<AttrSaleRecordRespVo.AttrSaleValueVo> valueVoList = attrSaleValueService.list(
                        new LambdaQueryWrapper<AttrSaleValue>()
                                .eq(AttrSaleValue::getAttrId, reqVo.getAttrId())
                )
                .stream()
                .map((attrSaleValue -> {
                    AttrSaleRecordRespVo.AttrSaleValueVo valueVo = new AttrSaleRecordRespVo.AttrSaleValueVo();
                    valueVo.setAttrId(attrSaleValue.getAttrId());
                    valueVo.setAttrValueId(attrSaleValue.getAttrValueId());
                    valueVo.setAttrValueName(attrSaleValue.getAttrValueName());
                    return valueVo;
                })).collect(Collectors.toList());
        return Result.success(valueVoList);
    }


    @PostMapping("queryByCategoryId")
    public Result<List<AttrSaleKey>> queryByCategoryId(@RequestBody PropertySaleQueryByCategoryIdReqVo req) {
        List<CategoryPropertySale> propertySales = categoryPropertySaleService.list(
                new LambdaQueryWrapper<CategoryPropertySale>()
                        .eq(CategoryPropertySale::getCategoryId, req.getCategoryId()));
        List<String> attrSaleIds = propertySales.stream().map(CategoryPropertySale::getPropertySaleId)
                .collect(Collectors.toList());
        List<AttrSaleKey> attrSaleKeys = attrSaleKeyService.list(
                new LambdaQueryWrapper<AttrSaleKey>()
                        .in(AttrSaleKey::getAttrId, attrSaleIds));
        Map<String, List<AttrSaleValue>> saleValueMap = attrSaleValueService.list(
                        new LambdaQueryWrapper<AttrSaleValue>()
                                .in(AttrSaleValue::getAttrId, attrSaleIds))
                .stream().collect(Collectors.groupingBy(AttrSaleValue::getAttrId));
        attrSaleKeys.forEach((item) -> item.setAttrSaleValues(saleValueMap.get(item.getAttrId())));
        return Result.success(attrSaleKeys);
    }

}
