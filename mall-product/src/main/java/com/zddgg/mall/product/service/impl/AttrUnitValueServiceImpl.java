package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zddgg.mall.product.entity.AttrUnitValue;
import com.zddgg.mall.product.mapper.AttrUnitValueMapper;
import com.zddgg.mall.product.service.AttrUnitValueService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 86239
 * @description 针对表【property_store_value(属性库value表)】的数据库操作Service实现
 * @createDate 2022-12-01 21:34:39
 */
@Service
public class AttrUnitValueServiceImpl extends ServiceImpl<AttrUnitValueMapper, AttrUnitValue>
        implements AttrUnitValueService {

    @Override
    public List<AttrUnitValue> getListByAttrIds(List<String> attrIds) {
        if (CollectionUtils.isEmpty(attrIds)) {
            return new ArrayList<>();
        }
        return this.list(
                new LambdaQueryWrapper<AttrUnitValue>()
                        .in(AttrUnitValue::getAttrId, attrIds));
    }

    @Override
    public List<AttrUnitValue> getList(AttrUnitValue data) {
        return this.list(
                new LambdaQueryWrapper<AttrUnitValue>()
                        .eq(AttrUnitValue::getAttrId, data.getAttrId()));
    }

    @Override
    public void deleteByAttrId(String attrId) {
        if (StringUtils.isBlank(attrId)) {
            return;
        }
        this.remove(new LambdaQueryWrapper<AttrUnitValue>().eq(AttrUnitValue::getAttrId, attrId));
    }
}




