package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zddgg.mall.product.entity.SkuAttrSale;
import com.zddgg.mall.product.mapper.SkuAttrSaleMapMapper;
import com.zddgg.mall.product.service.SkuAttrSaleMapService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 86239
 * @description 针对表【sku_attr_sale_map(SKU-销售属性表)】的数据库操作Service实现
 * @createDate 2023-03-10 12:41:23
 */
@Service
public class SkuAttrSaleMapServiceImpl extends ServiceImpl<SkuAttrSaleMapMapper, SkuAttrSale>
        implements SkuAttrSaleMapService {

    @Override
    public List<SkuAttrSale> getListBySpuId(String spuId) {
        if (StringUtils.isBlank(spuId)) {
            return new ArrayList<>();
        }
        return this.list(
                new LambdaQueryWrapper<SkuAttrSale>()
                        .eq(SkuAttrSale::getSpuId, spuId)
        );
    }
}




