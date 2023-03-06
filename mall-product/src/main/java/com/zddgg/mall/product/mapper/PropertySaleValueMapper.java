package com.zddgg.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zddgg.mall.product.entity.PropertySaleValue;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 86239
 * @description 针对表【property_sale_value(销售属性value表)】的数据库操作Mapper
 * @createDate 2023-03-05 09:06:08
 * @Entity com.zddgg.mall.product.entity.PropertySaleValue
 */
@Mapper
@Repository
public interface PropertySaleValueMapper extends BaseMapper<PropertySaleValue> {

}




