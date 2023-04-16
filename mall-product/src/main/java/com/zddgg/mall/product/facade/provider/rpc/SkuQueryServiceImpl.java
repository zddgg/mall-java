package com.zddgg.mall.product.facade.provider.rpc;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.product.api.response.SkuInfo;
import com.zddgg.mall.product.api.service.SkuQueryService;
import com.zddgg.mall.product.entity.SkuMeta;
import com.zddgg.mall.product.service.SkuMetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@DubboService
@RequiredArgsConstructor
public class SkuQueryServiceImpl implements SkuQueryService {

    private final SkuMetaService skuMetaService;

    @Override
    public List<SkuInfo> batchQuery(List<String> skuIds) {
        if (CollectionUtils.isEmpty(skuIds)) {
            return new ArrayList<>();
        }
        List<SkuMeta> skuMetas = skuMetaService.list(
                new LambdaQueryWrapper<SkuMeta>()
                        .in(SkuMeta::getSkuId, skuIds)
        );
        return skuMetas.stream().map(skuMeta -> SkuInfo
                        .builder()
                        .storeId(skuMeta.getStoreId())
                        .spuId(skuMeta.getSpuId())
                        .skuId(skuMeta.getSkuId())
                        .skuName(skuMeta.getSkuName())
                        .retailPrice(skuMeta.getRetailPrice())
                        .thumbnail(skuMeta.getThumbnail())
                        .statusFlag(skuMeta.getStatusFlag())
                        .build())
                .toList();
    }

    @Override
    public SkuInfo queryBySkuId(String skuId) {
        if (StringUtils.isBlank(skuId)) {
            return null;
        }
        SkuMeta skuMeta = skuMetaService.getOne(
                new LambdaQueryWrapper<SkuMeta>()
                        .eq(SkuMeta::getSkuId, skuId));
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuMeta, skuInfo);
        return skuInfo;
    }
}
