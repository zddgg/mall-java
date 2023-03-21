package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zddgg.mall.product.entity.SkuMeta;
import com.zddgg.mall.product.mapper.SkuMetaMapper;
import com.zddgg.mall.product.service.SkuMetaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 86239
 * @description 针对表【sku_meta(SKU信息表)】的数据库操作Service实现
 * @createDate 2023-03-11 14:22:09
 */
@Service
public class SkuMetaServiceImpl extends ServiceImpl<SkuMetaMapper, SkuMeta>
        implements SkuMetaService {

    @Override
    public List<SkuMeta> getListBySpuId(String spuId) {
        if (StringUtils.isBlank(spuId)) {
            return new ArrayList<>();
        }
        return this.list(new LambdaQueryWrapper<SkuMeta>().eq(SkuMeta::getSpuId, spuId));
    }
}




