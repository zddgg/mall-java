package com.zddgg.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zddgg.mall.product.entity.AttrUnitValue;

import java.util.List;

/**
 * @author 86239
 * @description 针对表【property_store_value(属性库value表)】的数据库操作Service
 * @createDate 2022-12-01 21:34:39
 */
public interface AttrUnitValueService extends IService<AttrUnitValue> {

    List<AttrUnitValue> getListByAttrIds(List<String> attrIds);

    List<AttrUnitValue> getList(AttrUnitValue data);

    void deleteByAttrId(String attrId);

}
