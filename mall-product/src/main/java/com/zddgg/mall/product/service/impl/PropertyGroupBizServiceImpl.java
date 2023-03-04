package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.product.bean.PropertyGroupCreateVo;
import com.zddgg.mall.product.bean.PropertyGroupDetailReqVo;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.PropertyGroup;
import com.zddgg.mall.product.entity.PropertyGroupStore;
import com.zddgg.mall.product.entity.PropertyUnitKey;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.PropertyGroupBizService;
import com.zddgg.mall.product.service.PropertyGroupService;
import com.zddgg.mall.product.service.PropertyGroupStoreService;
import com.zddgg.mall.product.service.PropertyUnitBizService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PropertyGroupBizServiceImpl implements PropertyGroupBizService {

    private final PropertyGroupService propertyGroupService;

    private final PropertyGroupStoreService propertyGroupStoreService;

    private final PropertyUnitBizService propertyUnitBizService;

    public PropertyGroupBizServiceImpl(PropertyGroupService propertyGroupService,
                                       PropertyGroupStoreService propertyGroupStoreService,
                                       PropertyUnitBizService propertyUnitBizService) {
        this.propertyGroupService = propertyGroupService;
        this.propertyGroupStoreService = propertyGroupStoreService;
        this.propertyUnitBizService = propertyUnitBizService;
    }

    @Transactional
    @Override
    public void saveKeyAndValue(PropertyGroupCreateVo vo) {
        LambdaQueryWrapper<PropertyGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PropertyGroup::getPropertyGroupName, vo.getPropertyGroupName());
        PropertyGroup one = propertyGroupService.getOne(queryWrapper);
        if (Objects.nonNull(one)) {
            throw new BizException("属性组名称[" + vo.getPropertyGroupName() + "]已存在！");
        }
        PropertyGroup propertyGroup = new PropertyGroup();
        propertyGroup.setPropertyGroupNo(UUID.randomUUID().toString().replace("-", ""));
        propertyGroup.setPropertyGroupName(vo.getPropertyGroupName());
        propertyGroup.setStatus(StatusEnum.DISABLED.code);
        propertyGroupService.save(propertyGroup);

        ArrayList<PropertyGroupStore> saveList = new ArrayList<>();
        for (PropertyUnitKey propertyUnitKey : vo.getPropertyStoreList()) {
            PropertyGroupStore value = new PropertyGroupStore();
            value.setPropertyGroupNo(propertyGroup.getPropertyGroupNo());
            value.setPropertyStoreNo(propertyUnitKey.getUnitKeyId());
            value.setPropertyOrder(0);
            saveList.add(value);
        }
        propertyGroupStoreService.saveBatch(saveList);
    }

    @Transactional
    @Override
    public void edit(PropertyGroupCreateVo vo) {
        LambdaQueryWrapper<PropertyGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PropertyGroup::getPropertyGroupNo, vo.getPropertyGroupNo());
        PropertyGroup one = propertyGroupService.getOne(queryWrapper);
        if (Objects.isNull(one)) {
            throw new BizException("属性组不存在，请刷新列表！");
        }
        one.setPropertyGroupName(vo.getPropertyGroupName());
        propertyGroupService.updateById(one);

        propertyGroupStoreService.remove(
                new LambdaQueryWrapper<PropertyGroupStore>()
                        .eq(PropertyGroupStore::getPropertyGroupNo, one.getPropertyGroupNo()));

        ArrayList<PropertyGroupStore> saveList = new ArrayList<>();
        for (PropertyUnitKey propertyUnitKey : vo.getPropertyStoreList()) {
            PropertyGroupStore value = new PropertyGroupStore();
            value.setPropertyGroupNo(one.getPropertyGroupNo());
            value.setPropertyStoreNo(propertyUnitKey.getUnitKeyId());
            value.setPropertyOrder(0);
            saveList.add(value);
        }
        propertyGroupStoreService.saveBatch(saveList);
    }

    @Transactional
    @Override
    public void delete(PropertyGroupDetailReqVo reqVo) {
        propertyGroupService.remove(
                new LambdaQueryWrapper<PropertyGroup>()
                        .eq(PropertyGroup::getPropertyGroupNo, reqVo.getPropertyGroupNo()));
        propertyGroupStoreService.remove(
                new LambdaQueryWrapper<PropertyGroupStore>()
                        .eq(PropertyGroupStore::getPropertyGroupNo, reqVo.getPropertyGroupNo()));
    }

    @Override
    public List<PropertyGroup> getListAndRelatedByGroupNos(List<String> groupNos) {
        if (CollectionUtils.isEmpty(groupNos)) {
            return new ArrayList<>();
        }
        List<PropertyGroup> propertyGroups = propertyGroupService.list(
                new LambdaQueryWrapper<PropertyGroup>()
                        .in(PropertyGroup::getPropertyGroupNo, groupNos));
        List<PropertyGroupStore> propertyGroupStores = propertyGroupStoreService.list(
                new LambdaQueryWrapper<PropertyGroupStore>()
                        .in(PropertyGroupStore::getPropertyGroupNo, groupNos));
        if (!CollectionUtils.isEmpty(propertyGroupStores)) {
            List<String> storeNos = propertyGroupStores.stream()
                    .map(PropertyGroupStore::getPropertyStoreNo).collect(Collectors.toList());
            Map<String, List<String>> group2StoreMap = propertyGroupStores.stream()
                    .collect(Collectors.groupingBy(PropertyGroupStore::getPropertyGroupNo,
                            Collectors.mapping(PropertyGroupStore::getPropertyStoreNo, Collectors.toList())));
            List<PropertyUnitKey> propertyUnitKeys = propertyUnitBizService.getListAndRelatedByPropertyNos(storeNos);
            Map<String, PropertyUnitKey> storeMap = propertyUnitKeys
                    .stream()
                    .collect(Collectors.toMap(PropertyUnitKey::getUnitKeyId,
                            propertyStoreRespVo -> propertyStoreRespVo));
            propertyGroups.forEach(propertyGroup -> {
                List<PropertyUnitKey> storeKeys = group2StoreMap
                        .getOrDefault(propertyGroup.getPropertyGroupNo(), new ArrayList<>())
                        .stream().map(storeMap::get).collect(Collectors.toList());
                propertyGroup.setPropertyStoreList(storeKeys);
            });

        }
        return propertyGroups;
    }
}
