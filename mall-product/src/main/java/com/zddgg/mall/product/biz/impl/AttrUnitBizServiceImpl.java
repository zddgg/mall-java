package com.zddgg.mall.product.biz.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.product.bean.attr.req.*;
import com.zddgg.mall.product.bean.attr.resp.AttrUnitRecordRespVo;
import com.zddgg.mall.product.biz.AttrUnitBizService;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.AttrGroupUnit;
import com.zddgg.mall.product.entity.AttrUnitKey;
import com.zddgg.mall.product.entity.AttrUnitValue;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.AttrGroupUnitService;
import com.zddgg.mall.product.service.AttrUnitKeyService;
import com.zddgg.mall.product.service.AttrUnitValueService;
import com.zddgg.mall.product.service.IdService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttrUnitBizServiceImpl implements AttrUnitBizService {

    private final IdService idService;

    private final AttrUnitKeyService attrUnitKeyService;

    private final AttrUnitValueService attrUnitValueService;

    private final AttrGroupUnitService attrGroupUnitService;

    @Override
    public Page<AttrUnitRecordRespVo> getAttrUnitRecordPage(AttrUnitRecordPageReqVo req) {
        Page<AttrUnitRecordRespVo> result = new Page<>();
        AttrUnitKey query = new AttrUnitKey();
        query.setAttrId(req.getAttrId());
        query.setAttrName(req.getAttrName());
        Page<AttrUnitKey> page = attrUnitKeyService.page(query, req);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            List<AttrUnitRecordRespVo> unitRecords = getRecordList(page.getRecords());
            result.setRecords(unitRecords);
            result.setPages(page.getPages());
            result.setCurrent(page.getCurrent());
            result.setSize(page.getSize());
            result.setTotal(page.getTotal());
        }
        return result;
    }

    @Override
    public AttrUnitRecordRespVo detail(AttrUnitDetailReqVo req) {
        if (StringUtils.isBlank(req.getAttrId())) {
            return null;
        }
        AttrUnitKey keyQuery = new AttrUnitKey();
        keyQuery.setAttrId(req.getAttrId());
        AttrUnitKey attrUnitKey = attrUnitKeyService.getOne(keyQuery);
        if (Objects.isNull(attrUnitKey)) {
            throw new BizException("属性库查询失败！");
        }
        AttrUnitValue valueQuery = new AttrUnitValue();
        valueQuery.setAttrId(req.getAttrId());
        List<AttrUnitRecordRespVo.AttrUnitValueVo> attrUnitValueVos = attrUnitValueService.getList(valueQuery)
                .stream()
                .map(attrUnitValue -> {
                    AttrUnitRecordRespVo.AttrUnitValueVo attrUnitValueVo = new AttrUnitRecordRespVo.AttrUnitValueVo();
                    attrUnitValueVo.setAttrId(attrUnitValue.getAttrId());
                    attrUnitValueVo.setAttrValueName(attrUnitValue.getAttrValueName());
                    attrUnitValueVo.setAttrValueOrder(attrUnitValue.getAttrValueOrder());
                    return attrUnitValueVo;
                })
                .collect(Collectors.toList());
        AttrUnitRecordRespVo result = new AttrUnitRecordRespVo();
        result.setAttrId(attrUnitKey.getAttrId());
        result.setAttrName(attrUnitKey.getAttrName());
        result.setUnit(attrUnitKey.getUnit());
        result.setFormShowType(attrUnitKey.getFormShowType());
        result.setStatus(attrUnitKey.getStatus());
        result.setAttrUnitValues(attrUnitValueVos);
        return result;
    }

    @Override
    public void create(AttrUnitCreateReqVo req) {
        AttrUnitKey attrUnitKeyQuery = new AttrUnitKey();
        attrUnitKeyQuery.setAttrName(req.getAttrName());
        AttrUnitKey one = attrUnitKeyService.getOne(attrUnitKeyQuery);
        if (Objects.nonNull(one)) {
            throw new BizException("属性名称[" + req.getAttrName() + "]已录入属性库，请检查属性名称！");
        }
        AttrUnitKey saveKey = new AttrUnitKey();
        saveKey.setAttrId(idService.getId());
        saveKey.setAttrName(req.getAttrName());
        saveKey.setUnit(req.getUnit());
        saveKey.setFormShowType(req.getFormShowType());
        saveKey.setStatus(StatusEnum.ENABLED.code);
        attrUnitKeyService.save(saveKey);

        ArrayList<AttrUnitValue> valueSaveList = new ArrayList<>();
        for (AttrUnitCreateReqVo.AttrUnitValueVo valueVo : req.getAttrUnitValues()) {
            AttrUnitValue value = new AttrUnitValue();
            value.setAttrId(saveKey.getAttrId());
            value.setAttrValueName(valueVo.getAttrValueName());
            value.setAttrValueOrder(valueVo.getAttrValueOrder());
            valueSaveList.add(value);
        }
        attrUnitValueService.saveBatch(valueSaveList);
    }

    @Override
    public void update(AttrUnitUpdateReqVo req) {
        AttrUnitKey keyQuery = new AttrUnitKey();
        keyQuery.setAttrId(req.getAttrId());
        AttrUnitKey one = attrUnitKeyService.getOne(keyQuery);
        if (Objects.isNull(one)) {
            throw new BizException("属性不存在，请刷新列表！");
        }
        one.setAttrName(req.getAttrName());
        one.setUnit(req.getUnit());
        one.setFormShowType(req.getFormShowType());
        attrUnitKeyService.updateById(one);

        attrUnitValueService.deleteByAttrId(one.getAttrId());

        ArrayList<AttrUnitValue> valueSaveList = new ArrayList<>();
        for (AttrUnitRecordRespVo.AttrUnitValueVo valueVo : req.getAttrUnitValues()) {
            AttrUnitValue value = new AttrUnitValue();
            value.setAttrId(one.getAttrId());
            value.setAttrValueName(valueVo.getAttrValueName());
            value.setAttrValueOrder(valueVo.getAttrValueOrder());
            valueSaveList.add(value);
        }
        attrUnitValueService.saveBatch(valueSaveList);
    }

    @Override
    public void delete(AttrUnitDeleteReqVo req) {
        // 属性组关联校验
        AttrGroupUnit groupUnitQuery = new AttrGroupUnit();
        groupUnitQuery.setAttrId(req.getAttrId());
        AttrGroupUnit one = attrGroupUnitService.getOne(groupUnitQuery);
        if (Objects.nonNull(one)) {
            throw new BizException("属性组 [" + one.getGroupId() + " ]已关联该属性，请先解除绑定！");
        }
        attrUnitKeyService.deleteByAttrId(req.getAttrId());
        attrUnitValueService.deleteByAttrId(req.getAttrId());
    }

    @Override
    public List<AttrUnitRecordRespVo> getRecordList(List<AttrUnitKey> unitKeyList) {
        List<String> attrIds = unitKeyList.stream().map(AttrUnitKey::getAttrId)
                .collect(Collectors.toList());
        List<AttrUnitValue> attrUnitValues = attrUnitValueService.getListByAttrIds(attrIds);
        Map<String, List<AttrUnitRecordRespVo.AttrUnitValueVo>> attrIdMap = attrUnitValues.stream()
                .map(attrUnitValue -> {
                    AttrUnitRecordRespVo.AttrUnitValueVo attrUnitValueVo = new AttrUnitRecordRespVo.AttrUnitValueVo();
                    attrUnitValueVo.setAttrId(attrUnitValue.getAttrId());
                    attrUnitValueVo.setAttrValueName(attrUnitValue.getAttrValueName());
                    attrUnitValueVo.setAttrValueOrder(attrUnitValue.getAttrValueOrder());
                    return attrUnitValueVo;
                })
                .collect(Collectors.groupingBy(AttrUnitRecordRespVo.AttrUnitValueVo::getAttrId));
        return unitKeyList.stream()
                .map(attrUnitKey -> {
                    AttrUnitRecordRespVo attrUnitRecordRespVo = new AttrUnitRecordRespVo();
                    attrUnitRecordRespVo.setAttrId(attrUnitKey.getAttrId());
                    attrUnitRecordRespVo.setAttrName(attrUnitKey.getAttrName());
                    attrUnitRecordRespVo.setUnit(attrUnitKey.getUnit());
                    attrUnitRecordRespVo.setFormShowType(attrUnitKey.getFormShowType());
                    attrUnitRecordRespVo.setStatus(attrUnitKey.getStatus());
                    attrUnitRecordRespVo.setAttrUnitValues(attrIdMap.get(attrUnitKey.getAttrId()));
                    return attrUnitRecordRespVo;
                }).collect(Collectors.toList());
    }
}
