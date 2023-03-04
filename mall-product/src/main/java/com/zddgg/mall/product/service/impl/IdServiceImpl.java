package com.zddgg.mall.product.service.impl;

import com.zddgg.mall.product.service.IdService;
import com.zddgg.mall.product.utils.IdUtil;
import org.springframework.stereotype.Service;

@Service
public class IdServiceImpl implements IdService {
    @Override
    public String getId() {
        return IdUtil.getId();
    }
}
