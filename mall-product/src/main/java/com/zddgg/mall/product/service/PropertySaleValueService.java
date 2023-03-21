package com.zddgg.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zddgg.mall.product.entity.PropertySaleValue;

import java.util.List;

/**
 * @author 86239
 * @description 针对表【property_sale_value(销售属性value表)】的数据库操作Service
 * @createDate 2023-03-05 09:06:08
 */
public interface PropertySaleValueService extends IService<PropertySaleValue> {
    List<PropertySaleValue> getListByAttrIds(List<String> ids);
}
