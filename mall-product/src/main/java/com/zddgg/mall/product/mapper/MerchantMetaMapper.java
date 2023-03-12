package com.zddgg.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zddgg.mall.product.entity.MerchantMeta;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MerchantMetaMapper extends BaseMapper<MerchantMeta> {
}
