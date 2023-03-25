package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zddgg.mall.common.request.PaginationReq;
import com.zddgg.mall.product.entity.AttrUnitKey;
import com.zddgg.mall.product.mapper.AttrUnitKeyMapper;
import com.zddgg.mall.product.service.AttrUnitKeyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author 86239
 * @description 针对表【property_store_key(属性库key表)】的数据库操作Service实现
 * @createDate 2022-12-01 21:38:42
 */
@Service
public class AttrUnitKeyServiceImpl extends ServiceImpl<AttrUnitKeyMapper, AttrUnitKey>
        implements AttrUnitKeyService {

    @Override
    public Page<AttrUnitKey> page(AttrUnitKey data, PaginationReq paginationReq) {
        return this.page(Page.of(paginationReq.getCurrent(), paginationReq.getPageSize()),
                new LambdaQueryWrapper<AttrUnitKey>()
                        .eq(StringUtils.isNoneBlank(data.getAttrId()), AttrUnitKey::getAttrId, data.getAttrId())
                        .like(StringUtils.isNoneBlank(data.getAttrName()), AttrUnitKey::getAttrName, data.getAttrName())
        );
    }

    @Override
    public AttrUnitKey getOne(AttrUnitKey data) {
        return this.getOne(
                new LambdaQueryWrapper<AttrUnitKey>()
                        .eq(StringUtils.isNoneBlank(data.getAttrId()), AttrUnitKey::getAttrId, data.getAttrId())
                        .eq(StringUtils.isNoneBlank(data.getAttrName()), AttrUnitKey::getAttrName, data.getAttrName())
        );
    }

    @Override
    public void deleteByAttrId(String attrId) {
        if (StringUtils.isBlank(attrId)) {
            return;
        }
        this.remove(new LambdaQueryWrapper<AttrUnitKey>().eq(AttrUnitKey::getAttrId, attrId));
    }
}




