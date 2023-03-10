package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.product.bean.PropertyUnitCreateReqVo;
import com.zddgg.mall.product.bean.PropertyUnitDeleteReqVo;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.PropertyGroupUnit;
import com.zddgg.mall.product.entity.PropertyUnitKey;
import com.zddgg.mall.product.entity.PropertyUnitValue;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.PropertyGroupStoreService;
import com.zddgg.mall.product.service.PropertyStoreValueService;
import com.zddgg.mall.product.service.PropertyUnitBizService;
import com.zddgg.mall.product.service.PropertyUnitKeyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PropertyUnitBizServiceImpl implements PropertyUnitBizService {

    private final PropertyUnitKeyService propertyUnitKeyService;

    private final PropertyStoreValueService propertyStoreValueService;

    private final PropertyGroupStoreService propertyGroupStoreService;

    public PropertyUnitBizServiceImpl(PropertyUnitKeyService propertyUnitKeyService,
                                      PropertyStoreValueService propertyStoreValueService,
                                      PropertyGroupStoreService propertyGroupStoreService) {
        this.propertyUnitKeyService = propertyUnitKeyService;
        this.propertyStoreValueService = propertyStoreValueService;
        this.propertyGroupStoreService = propertyGroupStoreService;
    }

    @Transactional
    @Override
    public void saveKeyAndValue(PropertyUnitCreateReqVo vo) {
        LambdaQueryWrapper<PropertyUnitKey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PropertyUnitKey::getUnitKeyName, vo.getUnitKeyName());
        PropertyUnitKey one = propertyUnitKeyService.getOne(queryWrapper);
        if (Objects.nonNull(one)) {
            throw new BizException("????????????[" + vo.getUnitKeyName() + "]?????????????????????????????????????????????");
        }
        PropertyUnitKey saveStoreKey = new PropertyUnitKey();
        saveStoreKey.setUnitKeyId(UUID.randomUUID().toString().replace("-", ""));
        saveStoreKey.setUnitKeyName(vo.getUnitKeyName());
        saveStoreKey.setUnitKeyUnit(vo.getUnitKeyUnit());
        saveStoreKey.setFormShowType(vo.getFormShowType());
        saveStoreKey.setStatus(StatusEnum.DISABLED.code);
        propertyUnitKeyService.save(saveStoreKey);

        ArrayList<PropertyUnitValue> valueSaveList = new ArrayList<>();
        for (PropertyUnitCreateReqVo.PropertyUnitValue propertyUnitValue : vo.getPropertyUnitValues()) {
            PropertyUnitValue value = new PropertyUnitValue();
            value.setUnitKeyId(saveStoreKey.getUnitKeyId());
            value.setUnitValue(propertyUnitValue.getUnitValue());
            value.setUnitValueOrder(propertyUnitValue.getUnitValueOrder());
            valueSaveList.add(value);
        }
        propertyStoreValueService.saveBatch(valueSaveList);
    }

    @Transactional
    @Override
    public void edit(PropertyUnitCreateReqVo vo) {
        LambdaQueryWrapper<PropertyUnitKey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PropertyUnitKey::getUnitKeyId, vo.getUnitKeyId());
        PropertyUnitKey one = propertyUnitKeyService.getOne(queryWrapper);
        if (Objects.isNull(one)) {
            throw new BizException("????????????????????????????????????");
        }
        one.setUnitKeyName(vo.getUnitKeyName());
        one.setUnitKeyUnit(vo.getUnitKeyUnit());
        one.setFormShowType(vo.getFormShowType());
        propertyUnitKeyService.updateById(one);

        propertyStoreValueService.remove(
                new LambdaQueryWrapper<PropertyUnitValue>().eq(PropertyUnitValue::getUnitKeyId, one.getUnitKeyId()));

        ArrayList<PropertyUnitValue> valueSaveList = new ArrayList<>();
        for (PropertyUnitCreateReqVo.PropertyUnitValue propertyUnitValue : vo.getPropertyUnitValues()) {
            PropertyUnitValue value = new PropertyUnitValue();
            value.setUnitKeyId(one.getUnitKeyId());
            value.setUnitValue(propertyUnitValue.getUnitValue());
            value.setUnitValueOrder(propertyUnitValue.getUnitValueOrder());
            valueSaveList.add(value);
        }
        propertyStoreValueService.saveBatch(valueSaveList);
    }

    @Transactional
    @Override
    public void delete(PropertyUnitDeleteReqVo reqVo) {
        // ?????????????????????
        List<PropertyGroupUnit> stores = propertyGroupStoreService.list(
                new LambdaQueryWrapper<PropertyGroupUnit>()
                        .eq(PropertyGroupUnit::getUnitKeyId, reqVo.getUnitKeyId()));
        if (!CollectionUtils.isEmpty(stores)) {
            throw new BizException("????????? [" + stores.get(0).getPropertyGroupId() + " ]??????????????????????????????????????????");
        }
        propertyUnitKeyService.remove(
                new LambdaQueryWrapper<PropertyUnitKey>()
                        .eq(PropertyUnitKey::getUnitKeyId, reqVo.getUnitKeyId()));
        propertyStoreValueService.remove(
                new LambdaQueryWrapper<PropertyUnitValue>()
                        .eq(PropertyUnitValue::getUnitKeyId, reqVo.getUnitKeyId()));
    }

    @Override
    public List<PropertyUnitKey> getListAndRelatedByPropertyIds(List<String> propertyKeyNos) {
        if (CollectionUtils.isEmpty(propertyKeyNos)) {
            return new ArrayList<>();
        }
        List<PropertyUnitKey> propertyUnitKeys = propertyUnitKeyService.list(
                new LambdaQueryWrapper<PropertyUnitKey>()
                        .in(PropertyUnitKey::getUnitKeyId, propertyKeyNos));
        List<PropertyUnitValue> PropertyUnitValues = propertyStoreValueService.list(
                new LambdaQueryWrapper<PropertyUnitValue>()
                        .in(PropertyUnitValue::getUnitKeyId, propertyKeyNos));
        Map<String, List<PropertyUnitValue>> propertyKeyNoMap = PropertyUnitValues.stream()
                .collect(Collectors.groupingBy(PropertyUnitValue::getUnitKeyId));
        propertyUnitKeys.forEach(propertyStoreKey ->
                propertyStoreKey.setPropertyUnitValues(propertyKeyNoMap.get(propertyStoreKey.getUnitKeyId())));
        return propertyUnitKeys;
    }

    @Override
    public PropertyUnitKey getDetailAndRelatedByPropertyNo(String propertyNo) {
        if (StringUtils.isBlank(propertyNo)) {
            return null;
        }
        PropertyUnitKey propertyUnitKey = propertyUnitKeyService.getOne(
                new LambdaQueryWrapper<PropertyUnitKey>()
                        .eq(PropertyUnitKey::getUnitKeyId, propertyNo));
        if (Objects.isNull(propertyUnitKey) || StringUtils.isBlank(propertyUnitKey.getUnitKeyId())) {
            throw new BizException("????????????????????????");
        }
        List<PropertyUnitValue> PropertyUnitValues = propertyStoreValueService
                .list(new LambdaQueryWrapper<PropertyUnitValue>()
                        .eq(PropertyUnitValue::getUnitKeyId, propertyUnitKey.getUnitKeyId()));
        propertyUnitKey.setPropertyUnitValues(PropertyUnitValues);
        return propertyUnitKey;
    }
}
