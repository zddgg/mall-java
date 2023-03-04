package com.zddgg.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.voidtime.mall.product.entity.Brand;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author 86239
* @description 针对表【brand(品牌表)】的数据库操作Mapper
* @createDate 2022-11-26 18:53:45
* @Entity com.voidtime.mall.product.entity.Brand
*/
@Mapper
@Repository
public interface BrandMapper extends BaseMapper<Brand> {

}




