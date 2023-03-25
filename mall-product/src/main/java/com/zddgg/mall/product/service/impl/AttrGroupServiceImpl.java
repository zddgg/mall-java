package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zddgg.mall.common.request.PaginationReq;
import com.zddgg.mall.product.entity.AttrGroup;
import com.zddgg.mall.product.mapper.AttrGroupMapper;
import com.zddgg.mall.product.service.AttrGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author 86239
 * @description 针对表【property_group(属性组表)】的数据库操作Service实现
 * @createDate 2022-12-04 08:17:07
 */
@Service
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupMapper, AttrGroup>
        implements AttrGroupService {

    @Override
    public Page<AttrGroup> page(AttrGroup data, PaginationReq page) {
        return this.page(Page.of(page.getCurrent(), page.getPageSize()),
                new LambdaQueryWrapper<AttrGroup>()
                        .eq(StringUtils.isNoneBlank(data.getGroupId()), AttrGroup::getGroupId, data.getGroupId())
                        .eq(StringUtils.isNoneBlank(data.getGroupName()), AttrGroup::getGroupName, data.getGroupName())
        );
    }

    @Override
    public AttrGroup getOne(AttrGroup data) {
        return this.getOne(
                new LambdaQueryWrapper<AttrGroup>()
                        .eq(StringUtils.isNoneBlank(data.getGroupId()), AttrGroup::getGroupId, data.getGroupId())
                        .eq(StringUtils.isNoneBlank(data.getGroupName()), AttrGroup::getGroupName, data.getGroupId())
        );
    }
}




