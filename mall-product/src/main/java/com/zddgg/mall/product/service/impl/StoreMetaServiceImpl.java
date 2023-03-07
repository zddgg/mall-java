package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zddgg.mall.product.entity.StoreMeta;
import com.zddgg.mall.product.mapper.StoreMetaMapper;
import com.zddgg.mall.product.service.StoreMetaService;
import org.springframework.stereotype.Service;

@Service
public class StoreMetaServiceImpl extends ServiceImpl<StoreMetaMapper, StoreMeta>
        implements StoreMetaService {
}
