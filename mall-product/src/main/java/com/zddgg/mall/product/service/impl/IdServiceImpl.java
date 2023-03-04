package com.zddgg.mall.product.service.impl;

import com.voidtime.mall.product.service.IdService;
import com.voidtime.mall.product.utils.IdUtil;
import org.springframework.stereotype.Service;

@Service
public class IdServiceImpl implements IdService {
    @Override
    public String getId() {
        return IdUtil.getId();
    }
}
