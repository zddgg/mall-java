package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zddgg.mall.product.entity.AttrGroupUnit;
import com.zddgg.mall.product.mapper.AttrGroupUnitMapper;
import com.zddgg.mall.product.service.AttrGroupUnitService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 86239
 * @description 针对表【property_group_store(属性组-属性库关系表)】的数据库操作Service实现
 * @createDate 2022-12-04 08:17:07
 */
@Service
public class AttrGroupUnitServiceImpl extends ServiceImpl<AttrGroupUnitMapper, AttrGroupUnit>
        implements AttrGroupUnitService {

    @Override
    public AttrGroupUnit getOne(AttrGroupUnit data) {
        return this.getOne(
                new LambdaQueryWrapper<AttrGroupUnit>()
                        .eq(StringUtils.isNoneBlank(data.getGroupId()), AttrGroupUnit::getGroupId, data.getGroupId())
                        .eq(StringUtils.isNoneBlank(data.getAttrId()), AttrGroupUnit::getAttrId, data.getGroupId())
        );
    }

    @Override
    public List<AttrGroupUnit> getListByGroupIds(List<String> groupIds) {
        if (CollectionUtils.isEmpty(groupIds)) {
            return new ArrayList<>();
        }
        return this.list(
                new LambdaQueryWrapper<AttrGroupUnit>()
                        .in(AttrGroupUnit::getGroupId, groupIds)
        );
    }

    @Override
    public List<AttrGroupUnit> getListByGroupId(String groupId) {
        if (StringUtils.isBlank(groupId)) {
            return null;
        }
        return this.list(
                new LambdaQueryWrapper<AttrGroupUnit>()
                        .eq(AttrGroupUnit::getGroupId, groupId)
        );
    }

    @Override
    public void deleteByGroupIdAndAttrId(AttrGroupUnit data) {
        this.remove(
                new LambdaQueryWrapper<AttrGroupUnit>()
                        .eq(AttrGroupUnit::getGroupId, data.getGroupId())
                        .eq(AttrGroupUnit::getAttrId, data.getAttrId())
        );
    }


}




