package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.voidtime.mall.product.bean.PropertyUnitCreateReqVo;
import com.voidtime.mall.product.bean.PropertyUnitDeleteReqVo;
import com.voidtime.mall.product.biz.service.PropertyUnitBizService;
import com.voidtime.mall.product.constant.StatusEnum;
import com.voidtime.mall.product.entity.PropertyGroupStore;
import com.voidtime.mall.product.entity.PropertyUnitKey;
import com.voidtime.mall.product.entity.propertyUnitValue;
import com.voidtime.mall.product.exception.BizException;
import com.voidtime.mall.product.service.PropertyGroupStoreService;
import com.voidtime.mall.product.service.PropertyStoreValueService;
import com.voidtime.mall.product.service.PropertyUnitKeyService;
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
            throw new BizException("属性名称[" + vo.getUnitKeyName() + "]已录入属性库，请检查属性名称！");
        }
        PropertyUnitKey saveStoreKey = new PropertyUnitKey();
        saveStoreKey.setUnitKeyId(UUID.randomUUID().toString().replace("-", ""));
        saveStoreKey.setUnitKeyName(vo.getUnitKeyName());
        saveStoreKey.setUnitKeyUnit(vo.getUnitKeyUnit());
        saveStoreKey.setFormShowType(vo.getFormShowType());
        saveStoreKey.setStatus(StatusEnum.DISABLED.code);
        propertyUnitKeyService.save(saveStoreKey);

        ArrayList<propertyUnitValue> valueSaveList = new ArrayList<>();
        for (PropertyUnitCreateReqVo.PropertyUnitValue propertyUnitValue : vo.getPropertyUnitValues()) {
            propertyUnitValue value = new propertyUnitValue();
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
            throw new BizException("属性不存在，请刷新列表！");
        }
        one.setUnitKeyName(vo.getUnitKeyName());
        one.setUnitKeyUnit(vo.getUnitKeyUnit());
        one.setFormShowType(vo.getFormShowType());
        propertyUnitKeyService.updateById(one);

        propertyStoreValueService.remove(
                new LambdaQueryWrapper<propertyUnitValue>().eq(propertyUnitValue::getUnitKeyId, one.getUnitKeyId()));

        ArrayList<propertyUnitValue> valueSaveList = new ArrayList<>();
        for (PropertyUnitCreateReqVo.PropertyUnitValue propertyUnitValue : vo.getPropertyUnitValues()) {
            propertyUnitValue value = new propertyUnitValue();
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
        // 属性组关联校验
        List<PropertyGroupStore> stores = propertyGroupStoreService.list(
                new LambdaQueryWrapper<PropertyGroupStore>()
                        .eq(PropertyGroupStore::getPropertyStoreNo, reqVo.getUnitKeyId()));
        if (!CollectionUtils.isEmpty(stores)) {
            throw new BizException("属性组 [" + stores.get(0).getPropertyGroupNo() + " ]已关联该属性，请先解除绑定！");
        }
        propertyUnitKeyService.remove(
                new LambdaQueryWrapper<PropertyUnitKey>()
                        .eq(PropertyUnitKey::getUnitKeyId, reqVo.getUnitKeyId()));
        propertyStoreValueService.remove(
                new LambdaQueryWrapper<propertyUnitValue>()
                        .eq(propertyUnitValue::getUnitKeyId, reqVo.getUnitKeyId()));
    }

    @Override
    public List<PropertyUnitKey> getListAndRelatedByPropertyNos(List<String> propertyKeyNos) {
        if (CollectionUtils.isEmpty(propertyKeyNos)) {
            return new ArrayList<>();
        }
        List<PropertyUnitKey> propertyUnitKeys = propertyUnitKeyService.list(
                new LambdaQueryWrapper<PropertyUnitKey>()
                        .in(PropertyUnitKey::getUnitKeyId, propertyKeyNos));
        List<propertyUnitValue> propertyUnitValues = propertyStoreValueService.list(
                new LambdaQueryWrapper<propertyUnitValue>()
                        .in(propertyUnitValue::getUnitKeyId, propertyKeyNos));
        Map<String, List<propertyUnitValue>> propertyKeyNoMap = propertyUnitValues.stream()
                .collect(Collectors.groupingBy(propertyUnitValue::getUnitKeyId));
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
            throw new BizException("属性库查询失败！");
        }
        List<propertyUnitValue> propertyUnitValues = propertyStoreValueService
                .list(new LambdaQueryWrapper<propertyUnitValue>()
                        .eq(propertyUnitValue::getUnitKeyId, propertyUnitKey.getUnitKeyId()));
        propertyUnitKey.setPropertyUnitValues(propertyUnitValues);
        return propertyUnitKey;
    }
}
