package com.zddgg.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zddgg.mall.product.entity.MerchantMeta;
import com.zddgg.mall.product.mapper.MerchantMetaMapper;
import com.zddgg.mall.product.service.MerchantMetaService;
import org.springframework.stereotype.Service;

@Service
public class MerchantMetaServiceImpl extends ServiceImpl<MerchantMetaMapper, MerchantMeta>
        implements MerchantMetaService {
}
