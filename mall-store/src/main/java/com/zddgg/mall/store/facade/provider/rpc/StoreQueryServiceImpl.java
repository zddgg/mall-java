package com.zddgg.mall.store.facade.provider.rpc;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.store.api.response.StoreInfo;
import com.zddgg.mall.store.api.service.StoreQueryService;
import com.zddgg.mall.store.entity.StoreMeta;
import com.zddgg.mall.store.service.StoreMetaService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@DubboService
@RequiredArgsConstructor
public class StoreQueryServiceImpl implements StoreQueryService {

    private final StoreMetaService storeMetaService;

    @Override
    public List<StoreInfo> batchQuery(List<String> storeIds) {
        if (CollectionUtils.isEmpty(storeIds)) {
            return new ArrayList<>();
        }
        List<StoreMeta> storeMetas = storeMetaService.list(
                new LambdaQueryWrapper<StoreMeta>()
                        .in(StoreMeta::getStoreId, storeIds)
        );
        return storeMetas.stream().map(storeMeta -> StoreInfo.builder()
                .storeId(storeMeta.getStoreId())
                .storeName(storeMeta.getStoreName())
                .build()).toList();
    }
}
