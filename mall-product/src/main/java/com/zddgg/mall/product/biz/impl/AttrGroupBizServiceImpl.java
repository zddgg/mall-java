package com.zddgg.mall.product.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.product.bean.attr.req.*;
import com.zddgg.mall.product.bean.attr.resp.AttrGroupRecordRespVo;
import com.zddgg.mall.product.bean.attr.resp.AttrUnitRecordRespVo;
import com.zddgg.mall.product.biz.AttrGroupBizService;
import com.zddgg.mall.product.biz.AttrUnitBizService;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.AttrGroup;
import com.zddgg.mall.product.entity.AttrGroupUnit;
import com.zddgg.mall.product.entity.AttrUnitKey;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.AttrGroupService;
import com.zddgg.mall.product.service.AttrGroupUnitService;
import com.zddgg.mall.product.service.AttrUnitKeyService;
import com.zddgg.mall.product.service.IdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttrGroupBizServiceImpl implements AttrGroupBizService {

    private final IdService idService;

    private final AttrGroupService attrGroupService;

    private final AttrGroupUnitService attrGroupUnitService;

    private final AttrUnitKeyService attrUnitKeyService;

    private final AttrUnitBizService attrUnitBizService;

    @Override
    public Page<AttrGroupRecordRespVo> getAttrGroupRecordPage(AttrGroupRecordPageReqVo req) {
        Page<AttrGroupRecordRespVo> result = new Page<>();
        AttrGroup groupQuery = new AttrGroup();
        groupQuery.setGroupId(req.getGroupId());
        groupQuery.setGroupName(req.getGroupName());
        Page<AttrGroup> page = attrGroupService.page(groupQuery, req);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            List<String> groupIds = page.getRecords().stream()
                    .map(AttrGroup::getGroupId)
                    .collect(Collectors.toList());
            List<AttrGroupUnit> groupUnitList = attrGroupUnitService.getListByGroupIds(groupIds);
            Map<String, List<AttrGroupUnit>> groupId2GroupUnitList = groupUnitList.stream().collect(Collectors.groupingBy(AttrGroupUnit::getGroupId));
            List<String> attrIds = groupUnitList.stream().map(AttrGroupUnit::getAttrId).collect(Collectors.toList());
            List<AttrUnitKey> unitKeyList = attrUnitKeyService.getListByAttrIds(attrIds);
            Map<String, AttrUnitRecordRespVo> attrIdMap2AttrUnitRecord = attrUnitBizService.getRecordList(unitKeyList)
                    .stream()
                    .collect(Collectors.toMap(AttrUnitRecordRespVo::getAttrId, v -> v, (v1, v2) -> v1));
            List<AttrGroupRecordRespVo> attrGroupRecordRespVoList = page.getRecords()
                    .stream()
                    .map(attrGroup -> {
                        List<AttrUnitRecordRespVo> attrUnitRecordRespList = groupId2GroupUnitList.getOrDefault(attrGroup.getGroupId(), new ArrayList<>())
                                .stream()
                                .map(attrGroupUnit -> attrIdMap2AttrUnitRecord.get(attrGroupUnit.getAttrId()))
                                .collect(Collectors.toList());
                        AttrGroupRecordRespVo attrGroupRecordRespVo = new AttrGroupRecordRespVo();
                        attrGroupRecordRespVo.setGroupId(attrGroup.getGroupId());
                        attrGroupRecordRespVo.setGroupName(attrGroup.getGroupName());
                        attrGroupRecordRespVo.setStatus(attrGroup.getStatus());
                        attrGroupRecordRespVo.setAttrUnitRecords(attrUnitRecordRespList);
                        return attrGroupRecordRespVo;
                    }).collect(Collectors.toList());
            result.setRecords(attrGroupRecordRespVoList);
            result.setPages(page.getPages());
            result.setCurrent(page.getCurrent());
            result.setSize(page.getSize());
            result.setTotal(page.getTotal());
        }
        return result;
    }

    @Override
    public void create(AttrGroupCreateReqVo req) {
        AttrGroup attrGroup = new AttrGroup();
        attrGroup.setGroupName(req.getGroupName());
        AttrGroup one = attrGroupService.getOne(attrGroup);
        if (Objects.nonNull(one)) {
            throw new BizException("属性组名称[" + req.getGroupName() + "]已存在！");
        }
        attrGroup.setGroupId(idService.getId());
        attrGroup.setStatus(StatusEnum.DISABLED.code);
        attrGroupService.save(attrGroup);

        ArrayList<AttrGroupUnit> saveList = new ArrayList<>();
        for (String attrId : req.getAttrIds()) {
            AttrGroupUnit value = new AttrGroupUnit();
            value.setGroupId(attrGroup.getGroupId());
            value.setAttrId(attrId);
            value.setAttrUnitOrder(0);
            saveList.add(value);
        }
        attrGroupUnitService.saveBatch(saveList);
    }

    @Override
    public AttrGroupRecordRespVo detail(AttrGroupDetailReqVo req) {
        AttrGroup groupQuery = new AttrGroup();
        groupQuery.setGroupId(req.getGroupId());
        AttrGroup attrGroup = attrGroupService.getOne(groupQuery);
        List<AttrGroupUnit> groupUnitList = attrGroupUnitService.getListByGroupId(req.getGroupId());
        List<String> attrIds = groupUnitList.stream().map(AttrGroupUnit::getAttrId).collect(Collectors.toList());
        List<AttrUnitKey> unitKeyList = attrUnitKeyService.getListByAttrIds(attrIds);
        Map<String, AttrUnitRecordRespVo> attrIdMap2AttrUnitRecord = attrUnitBizService.getRecordList(unitKeyList)
                .stream()
                .collect(Collectors.toMap(AttrUnitRecordRespVo::getAttrId, v -> v, (v1, v2) -> v1));
        List<AttrUnitRecordRespVo> attrUnitRecordRespList = groupUnitList
                .stream()
                .map(attrGroupUnit -> attrIdMap2AttrUnitRecord.get(attrGroupUnit.getAttrId()))
                .collect(Collectors.toList());
        AttrGroupRecordRespVo attrGroupRecordRespVo = new AttrGroupRecordRespVo();
        attrGroupRecordRespVo.setGroupId(attrGroup.getGroupId());
        attrGroupRecordRespVo.setGroupName(attrGroup.getGroupName());
        attrGroupRecordRespVo.setStatus(attrGroup.getStatus());
        attrGroupRecordRespVo.setAttrUnitRecords(attrUnitRecordRespList);
        return attrGroupRecordRespVo;
    }

    @Override
    public void unBindAttrUnit(AttrGroupBindReqVo reqVo) {
        AttrGroupUnit attrGroupUnit = new AttrGroupUnit();
        attrGroupUnit.setGroupId(reqVo.getGroupId());
        attrGroupUnit.setAttrId(reqVo.getAttrId());
        attrGroupUnitService.deleteByGroupIdAndAttrId(attrGroupUnit);
    }

    @Override
    public void update(AttrGroupCreateReqVo req) {
        AttrGroup attrGroup = new AttrGroup();
        attrGroup.setGroupId(req.getGroupId());
        AttrGroup one = attrGroupService.getOne(attrGroup);
        if (Objects.isNull(one)) {
            throw new BizException("属性组信息已存在！");
        }
        attrGroup.setGroupName(req.getGroupName());
        attrGroupService.updateById(attrGroup);

        ArrayList<AttrGroupUnit> saveList = new ArrayList<>();
        for (String attrId : req.getAttrIds()) {
            AttrGroupUnit value = new AttrGroupUnit();
            value.setGroupId(attrGroup.getGroupId());
            value.setAttrId(attrId);
            value.setAttrUnitOrder(0);
            saveList.add(value);
        }
        attrGroupUnitService.saveBatch(saveList);
    }

    @Override
    public List<AttrUnitRecordRespVo> getBindAttrUnit(AttrGroupBindReqVo req) {
        List<AttrGroupUnit> groupUnitList = attrGroupUnitService.getListByGroupId(req.getGroupId());
        List<String> attrIds = groupUnitList.stream().map(AttrGroupUnit::getAttrId).collect(Collectors.toList());
        List<AttrUnitKey> unitKeyList = attrUnitKeyService.getListByAttrIds(attrIds);
        Map<String, AttrUnitRecordRespVo> attrIdMap2AttrUnitRecord = attrUnitBizService.getRecordList(unitKeyList)
                .stream()
                .collect(Collectors.toMap(AttrUnitRecordRespVo::getAttrId, v -> v, (v1, v2) -> v1));
        return groupUnitList
                .stream()
                .map(attrGroupUnit -> attrIdMap2AttrUnitRecord.get(attrGroupUnit.getAttrId()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(AttrGroupDeleteReqVo req) {
        attrGroupService.remove(
                new LambdaQueryWrapper<AttrGroup>()
                        .eq(AttrGroup::getGroupId, req.getGroupId()));
        attrGroupUnitService.remove(
                new LambdaQueryWrapper<AttrGroupUnit>()
                        .eq(AttrGroupUnit::getGroupId, req.getGroupId()));
    }
}
