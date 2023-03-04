package com.zddgg.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zddgg.mall.product.entity.propertyUnitValue;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 86239
 * @description 针对表【property_store_value(属性库value表)】的数据库操作Mapper
 * @createDate 2022-12-01 21:34:39
 * @Entity com.voidtime.mall.product.entity.PropertyStoreValue
 */
@Mapper
@Repository
public interface PropertyStoreValueMapper extends BaseMapper<propertyUnitValue> {

}




