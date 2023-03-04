package com.zddgg.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.voidtime.mall.product.entity.PropertyGroupStore;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 86239
 * @description 针对表【property_group_store(属性组-属性库关系表)】的数据库操作Mapper
 * @createDate 2022-12-04 08:17:07
 * @Entity com.voidtime.mall.product.entity.PropertyGroupStore
 */

@Mapper
@Repository
public interface PropertyGroupStoreMapper extends BaseMapper<PropertyGroupStore> {

}




