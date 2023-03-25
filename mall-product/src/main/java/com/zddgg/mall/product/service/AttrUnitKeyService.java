package com.zddgg.mall.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zddgg.mall.common.request.PaginationReq;
import com.zddgg.mall.product.entity.AttrUnitKey;

import java.util.List;

/**
 * @author 86239
 * @description 针对表【property_store_key(属性库key表)】的数据库操作Service
 * @createDate 2022-12-01 21:38:42
 */
public interface AttrUnitKeyService extends IService<AttrUnitKey> {

    Page<AttrUnitKey> page(AttrUnitKey data, PaginationReq paginationReq);

    AttrUnitKey getOne(AttrUnitKey data);

    List<AttrUnitKey> getListByAttrIds(List<String> attrIds);

    void deleteByAttrId(String attrId);
}
