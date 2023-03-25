package com.zddgg.mall.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zddgg.mall.common.request.PaginationReq;
import com.zddgg.mall.product.entity.AttrGroup;

/**
 * @author 86239
 * @description 针对表【property_group(属性组表)】的数据库操作Service
 * @createDate 2022-12-04 08:17:07
 */
public interface AttrGroupService extends IService<AttrGroup> {

    Page<AttrGroup> page(AttrGroup data, PaginationReq page);

    AttrGroup getOne(AttrGroup data);
}
