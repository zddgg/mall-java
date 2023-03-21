package com.zddgg.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zddgg.mall.product.entity.SkuAttrSaleMap;

import java.util.List;

/**
 * @author 86239
 * @description 针对表【sku_attr_sale_map(SKU-销售属性表)】的数据库操作Service
 * @createDate 2023-03-10 12:41:23
 */
public interface SkuAttrSaleMapService extends IService<SkuAttrSaleMap> {

    List<SkuAttrSaleMap> getListBySpuId(String spuId);
}
