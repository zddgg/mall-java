package com.zddgg.mall.oms.service.impl;

import com.zddgg.mall.oms.service.IdService;
import com.zddgg.mall.oms.utils.IdUtil;
import org.springframework.stereotype.Service;

@Service
public class IdServiceImpl implements IdService {
    @Override
    public String getId() {
        return IdUtil.getId();
    }
}
