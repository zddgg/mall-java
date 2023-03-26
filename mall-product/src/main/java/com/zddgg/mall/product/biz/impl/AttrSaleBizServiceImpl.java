package com.zddgg.mall.product.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.product.bean.attr.req.AttrSaleCreateReqVo;
import com.zddgg.mall.product.bean.attr.req.AttrSaleDetailReqVo;
import com.zddgg.mall.product.bean.attr.req.AttrSaleRecordPageReqVo;
import com.zddgg.mall.product.bean.attr.resp.AttrSaleRecordRespVo;
import com.zddgg.mall.product.biz.AttrSaleBizService;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.AttrSaleKey;
import com.zddgg.mall.product.entity.AttrSaleValue;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.AttrSaleKeyService;
import com.zddgg.mall.product.service.AttrSaleValueService;
import com.zddgg.mall.product.service.IdService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttrSaleBizServiceImpl implements AttrSaleBizService {

    private final IdService idService;

    private final AttrSaleKeyService attrSaleKeyService;

    private final AttrSaleValueService attrSaleValueService;

    @Override
    public Page<AttrSaleRecordRespVo> getAttrSaleRecordPage(AttrSaleRecordPageReqVo req) {
        Page<AttrSaleRecordRespVo> result = new Page<>();
        Page<AttrSaleKey> page = attrSaleKeyService.page(Page.of(req.getCurrent(), req.getPageSize()),
                new LambdaQueryWrapper<AttrSaleKey>()
                        .eq(StringUtils.isNoneBlank(req.getAttrId()),
                                AttrSaleKey::getAttrId, req.getAttrId())
                        .like(StringUtils.isNoneBlank(req.getAttrName()),
                                AttrSaleKey::getAttrName, req.getAttrName())
        );
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            List<String> keyIds = page.getRecords().stream().map(AttrSaleKey::getAttrId)
                    .collect(Collectors.toList());
            Map<String, List<AttrSaleRecordRespVo.AttrSaleValueVo>> saleValueVoMap = attrSaleValueService.list(
                            new LambdaQueryWrapper<AttrSaleValue>().in(AttrSaleValue::getAttrId, keyIds))
                    .stream()
                    .map((attrSaleValue -> {
                        AttrSaleRecordRespVo.AttrSaleValueVo attrSaleValueVo = new AttrSaleRecordRespVo.AttrSaleValueVo();
                        attrSaleValueVo.setAttrId(attrSaleValue.getAttrId());
                        attrSaleValueVo.setAttrValueId(attrSaleValue.getAttrId());
                        attrSaleValueVo.setAttrValueName(attrSaleValue.getAttrValueName());
                        return attrSaleValueVo;
                    }))
                    .collect(Collectors.groupingBy(AttrSaleRecordRespVo.AttrSaleValueVo::getAttrId));
            List<AttrSaleRecordRespVo> respVoList = page.getRecords().stream()
                    .map(attrSaleKey -> {
                        AttrSaleRecordRespVo respVo = new AttrSaleRecordRespVo();
                        respVo.setAttrId(attrSaleKey.getAttrId());
                        respVo.setAttrName(attrSaleKey.getAttrName());
                        respVo.setAttrSaleValues(saleValueVoMap.get(attrSaleKey.getAttrId()));
                        return respVo;
                    }).collect(Collectors.toList());
            result.setRecords(respVoList);
            result.setPages(page.getPages());
            result.setCurrent(page.getCurrent());
            result.setSize(page.getSize());
            result.setTotal(page.getTotal());
        }
        return result;
    }

    @Override
    public void create(AttrSaleCreateReqVo req) {
        LambdaQueryWrapper<AttrSaleKey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttrSaleKey::getAttrName, req.getAttrName());
        AttrSaleKey one = attrSaleKeyService.getOne(queryWrapper);
        if (Objects.nonNull(one)) {
            throw new BizException("销售属性名称[" + one.getAttrName() + "]已录入，请检查属性名称！");
        }
        AttrSaleKey saveSaleKey = new AttrSaleKey();
        saveSaleKey.setAttrId(idService.getId());
        saveSaleKey.setAttrName(req.getAttrName());
        saveSaleKey.setStatus(StatusEnum.ENABLED.code);
        attrSaleKeyService.save(saveSaleKey);

        List<AttrSaleValue> attrSaleValues = req.getAttrSaleValues().stream().map((item) -> {
            AttrSaleValue attrSaleValue = new AttrSaleValue();
            attrSaleValue.setAttrId(saveSaleKey.getAttrId());
            attrSaleValue.setAttrValueId(idService.getId());
            attrSaleValue.setAttrValueName(item.getAttrValueName());
            attrSaleValue.setAttrValueOrder(0);
            return attrSaleValue;
        }).collect(Collectors.toList());
        attrSaleValueService.saveBatch(attrSaleValues);
    }

    @Override
    public AttrSaleRecordRespVo detail(AttrSaleDetailReqVo req) {
        AttrSaleKey saleKey = attrSaleKeyService.getOne(
                new LambdaQueryWrapper<AttrSaleKey>()
                        .eq(AttrSaleKey::getAttrId, req.getAttrId())
        );
        if (Objects.isNull(saleKey)) {
            throw new BizException("属性信息查询失败！");
        }
        List<AttrSaleValue> saleValues = attrSaleValueService.list(
                new LambdaQueryWrapper<AttrSaleValue>()
                        .eq(AttrSaleValue::getAttrId, req.getAttrId()));
        List<AttrSaleRecordRespVo.AttrSaleValueVo> valueVoList = saleValues.stream()
                .map((attrSaleValue -> {
                    AttrSaleRecordRespVo.AttrSaleValueVo attrSaleValueVo = new AttrSaleRecordRespVo.AttrSaleValueVo();
                    attrSaleValueVo.setAttrId(attrSaleValue.getAttrId());
                    attrSaleValueVo.setAttrValueId(attrSaleValue.getAttrValueId());
                    attrSaleValueVo.setAttrValueName(attrSaleValue.getAttrValueName());
                    return attrSaleValueVo;
                }))
                .collect(Collectors.toList());
        AttrSaleRecordRespVo respVo = new AttrSaleRecordRespVo();
        respVo.setAttrId(saleKey.getAttrId());
        respVo.setAttrName(saleKey.getAttrName());
        respVo.setAttrSaleValues(valueVoList);
        return respVo;
    }
}
