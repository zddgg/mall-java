package com.zddgg.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zddgg.mall.product.entity.PropertyGroup;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 86239
 * @description 针对表【property_group(属性组表)】的数据库操作Mapper
 * @createDate 2022-12-04 08:17:07
 * @Entity com.voidtime.mall.product.entity.PropertyGroup
 */

@Mapper
@Repository
public interface PropertyGroupMapper extends BaseMapper<PropertyGroup> {

}




