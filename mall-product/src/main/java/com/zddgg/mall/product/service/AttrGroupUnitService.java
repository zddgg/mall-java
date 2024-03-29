package com.zddgg.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zddgg.mall.product.entity.AttrGroupUnit;

import java.util.List;

/**
 * @author 86239
 * @description 针对表【property_group_store(属性组-属性库关系表)】的数据库操作Service
 * @createDate 2022-12-04 08:17:08
 */
public interface AttrGroupUnitService extends IService<AttrGroupUnit> {

    AttrGroupUnit getOne(AttrGroupUnit data);

    List<AttrGroupUnit> getListByGroupIds(List<String> groupIds);

    List<AttrGroupUnit> getListByGroupId(String groupId);

    void deleteByGroupIdAndAttrId(AttrGroupUnit data);
}
