package com.zddgg.mall.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zddgg.mall.store.entity.StoreMeta;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author 86239
* @description 针对表【store_meta(店铺信息表)】的数据库操作Mapper
* @createDate 2023-04-16 18:45:14
* @Entity generator.entity.StoreMeta
*/
@Mapper
@Repository
public interface StoreMetaMapper extends BaseMapper<StoreMeta> {

}




