package com.zddgg.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zddgg.mall.product.entity.PropertyUnitKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 86239
 * @description 针对表【property_store_key(属性库key表)】的数据库操作Mapper
 * @createDate 2022-12-01 21:38:42
 * @Entity com.voidtime.mall.product.entity.PropertyStoreKey
 */
@Mapper
@Repository
public interface PropertyUnitKeyMapper extends BaseMapper<PropertyUnitKey> {

}




