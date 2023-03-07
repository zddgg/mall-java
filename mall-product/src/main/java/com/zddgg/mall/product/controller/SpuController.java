package com.zddgg.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.common.request.PaginationReq;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.SpuMeta;
import com.zddgg.mall.product.service.SpuMetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product/spu")
@RequiredArgsConstructor
public class SpuController {

    private final SpuMetaService spuMetaService;

    @PostMapping("list")
    public Result<Page<SpuMeta>> list(@RequestBody PaginationReq req) {
        Page<SpuMeta> page = new Page<>(req.getCurrent(), req.getPageSize());
        LambdaQueryWrapper<SpuMeta> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(SpuMeta::getStatusFlag, StatusEnum.DELETED.code);
        spuMetaService.page(page, queryWrapper);
        return Result.success(page);
    }
}
