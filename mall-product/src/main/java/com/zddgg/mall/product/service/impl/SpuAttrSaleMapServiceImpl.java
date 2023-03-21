package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zddgg.mall.product.entity.SpuAttrSaleMap;
import com.zddgg.mall.product.mapper.SpuAttrSaleMapMapper;
import com.zddgg.mall.product.service.SpuAttrSaleMapService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 86239
 * @description 针对表【spu_attr_sale_map(SPU-销售属性表)】的数据库操作Service实现
 * @createDate 2023-03-10 12:41:23
 */
@Service
public class SpuAttrSaleMapServiceImpl extends ServiceImpl<SpuAttrSaleMapMapper, SpuAttrSaleMap>
        implements SpuAttrSaleMapService {

    @Override
    public List<SpuAttrSaleMap> getListBySpuId(String spuId) {
        if (StringUtils.isBlank(spuId)) {
            return new ArrayList<>();
        }
        return this.list(
                new LambdaQueryWrapper<SpuAttrSaleMap>()
                        .eq(SpuAttrSaleMap::getSpuId, spuId)
        );
    }
}




