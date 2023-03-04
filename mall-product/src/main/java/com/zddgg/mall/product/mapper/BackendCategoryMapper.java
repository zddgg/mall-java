package com.zddgg.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zddgg.mall.product.entity.BackendCategory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 86239
 * @description 针对表【backend_category(后台类目表)】的数据库操作Mapper
 * @createDate 2022-11-30 17:38:36
 * @Entity com.voidtime.mall.product.entity.BackendCategory
 */
@Mapper
@Repository
public interface BackendCategoryMapper extends BaseMapper<BackendCategory> {

}




