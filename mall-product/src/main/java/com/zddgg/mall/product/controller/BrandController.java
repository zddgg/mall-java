package com.zddgg.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zddgg.mall.common.request.PaginationReq;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.BrandCreateVo;
import com.zddgg.mall.product.bean.BrandDetailVo;
import com.zddgg.mall.product.bean.BrandQueryVo;
import com.zddgg.mall.product.bean.SelectOptionData;
import com.zddgg.mall.product.constant.RespEnum;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.constant.brand.BrandLevel;
import com.zddgg.mall.product.entity.Brand;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.BrandService;
import com.zddgg.mall.product.service.IdService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    private final IdService idService;

    @PostMapping("list")
    public Result<Page<Brand>> list(@RequestBody PaginationReq paginationReq) {
        Page<Brand> page = new Page<>(paginationReq.getCurrent(), paginationReq.getPageSize());
        LambdaQueryWrapper<Brand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Brand::getStatus, StatusEnum.DELETED.code);
        brandService.page(page, queryWrapper);
        return Result.success(page);
    }

    @PostMapping("create")
    public Result<Object> create(@RequestBody BrandCreateVo brandCreateVo) {
        LambdaQueryWrapper<Brand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Brand::getBrandName, brandCreateVo.getBrandName());
        queryWrapper.eq(Brand::getForeignBrandName, brandCreateVo.getForeignBrandName());
        queryWrapper.eq(Brand::getShowName, brandCreateVo.getShowName());
        Brand one = brandService.getOne(queryWrapper);
        if (Objects.nonNull(one)) {
            throw new BizException(RespEnum.BRAND_EXIST);
        }

        Brand brand = new Brand();
        brand.setBrandId(idService.getId());
        brand.setBrandName(brandCreateVo.getBrandName());
        brand.setForeignBrandName(brandCreateVo.getForeignBrandName());
        brand.setShowName(brandCreateVo.getShowName());
        brand.setLevel(BrandLevel.NONE.code);
        brand.setLogo(brandCreateVo.getLogo());
        brand.setStatus(StatusEnum.DISABLED.code);

        brandService.save(brand);

        return Result.success();
    }

    @PostMapping("update")
    public Result<Object> update(@RequestBody BrandDetailVo brandDetailVo) {
        LambdaQueryWrapper<Brand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Brand::getBrandId, brandDetailVo.getBrandId());
        Brand one = brandService.getOne(queryWrapper);
        if (Objects.isNull(one)) {
            throw new BizException("品牌不存在！");
        }

        one.setBrandId(idService.getId());
        one.setBrandName(brandDetailVo.getBrandName());
        one.setForeignBrandName(brandDetailVo.getForeignBrandName());
        one.setShowName(brandDetailVo.getShowName());
        one.setLevel(brandDetailVo.getLevel());
        one.setLogo(brandDetailVo.getLogo());
        one.setStatus(brandDetailVo.getStatus());

        brandService.updateById(one);

        return Result.success();
    }

    @PostMapping("detail")
    public Result<Object> detail(@RequestBody @Validated BrandQueryVo brandQueryVo) {
        LambdaQueryWrapper<Brand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Brand::getBrandId, brandQueryVo.getBrandId());
        Brand brand = brandService.getOne(queryWrapper);
        if (Objects.isNull(brand)) {
            return Result.fail("品牌信息不存在");
        }
        return Result.success(brand);
    }

    @PostMapping("delete")
    public Result<Object> delete(@RequestBody @Validated BrandQueryVo brandQueryVo) {
        brandService.remove(
                new LambdaQueryWrapper<Brand>()
                        .eq(Brand::getBrandId, brandQueryVo.getBrandId()));
        return Result.success();
    }

    @PostMapping("options")
    public Result<List<SelectOptionData>> options() {
        List<Brand> brands = brandService.list();
        List<SelectOptionData> dataList = brands.stream()
                .map(brand -> {
                    SelectOptionData optionData = new SelectOptionData();
                    optionData.setLabel(brand.getBrandName());
                    optionData.setValue(brand.getBrandId());
                    return optionData;
                }).collect(Collectors.toList());
        return Result.success(dataList);
    }
}
