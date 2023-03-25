package com.zddgg.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zddgg.mall.product.entity.SkuMeta;

import java.util.List;

/**
 * @author 86239
 * @description 针对表【sku_meta(SKU信息表)】的数据库操作Service
 * @createDate 2023-03-11 14:22:09
 */
public interface SkuMetaService extends IService<SkuMeta> {

    public List<SkuMeta> getListBySpuId(String spuId);
}
