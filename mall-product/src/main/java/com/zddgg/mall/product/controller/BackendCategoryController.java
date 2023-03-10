package com.zddgg.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zddgg.mall.common.response.PaginationRes;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.*;
import com.zddgg.mall.product.constant.StatusEnum;
import com.zddgg.mall.product.entity.BackendCategory;
import com.zddgg.mall.product.entity.CategoryPropertySale;
import com.zddgg.mall.product.entity.PropertySaleKey;
import com.zddgg.mall.product.exception.BizException;
import com.zddgg.mall.product.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("backendCategory")
@RequiredArgsConstructor
public class BackendCategoryController {

    private final BackendCategoryService backendCategoryService;

    private final BackendCategoryBizService backendCategoryBizService;

    private final PropertySaleKeyService propertySaleKeyService;

    private final CategoryPropertySaleService categoryPropertySaleService;

    private final PropertySaleBizService propertySaleBizService;

    @PostMapping("list")
    public Result<PaginationRes<BackendCategoryNode>> list(@RequestBody BackendCategoryListVo vo) {
        return backendCategoryBizService.pageList(vo);
    }

    @PostMapping("tree")
    public Result<List<BackendCategoryNode>> tree(@RequestBody BackendCategoryTreeQueryVo vo) {
        List<BackendCategoryNode> list = backendCategoryBizService.getRootList(vo.getMaxLevel());
        if (vo.getRootHelp()) {
            List<BackendCategoryNode> result = new ArrayList<>();
            BackendCategoryNode root = new BackendCategoryNode();
            root.setCategoryId("0");
            root.setCategoryName("?????????");
            result.add(root);
            result.addAll(list);
            list = result;
        }
        return Result.success(list);
    }

    @PostMapping("create")
    public Result<Objects> create(@RequestBody BackendCategoryCreateReqVo reqVo) {
        backendCategoryBizService.create(reqVo);
        return Result.success();
    }

    @PostMapping("update")
    public Result<Objects> update(@RequestBody BackendCategoryCreateReqVo reqVo) {
        backendCategoryBizService.update(reqVo);
        return Result.success();
    }

    @PostMapping("detail")
    public Result<BackendCategoryDetail> detail(@RequestBody BackendCategoryDetailReqVo reqVo) {
        return Result.success(backendCategoryBizService.detail(reqVo.getCategoryId()));
    }

    @PostMapping("parentDetail")
    public Result<List<BackendCategoryDetail>> parentDetail(@RequestBody BackendCategoryDetailReqVo reqVo) {
        List<BackendCategoryDetail> respVo = backendCategoryBizService.parentDetail(reqVo.getCategoryId());
        return Result.success(respVo);
    }

    @PostMapping("parentAndSelfDetail")
    public Result<List<BackendCategoryDetail>> parentAndSelfDetail(@RequestBody BackendCategoryDetailReqVo reqVo) {
        List<BackendCategoryDetail> respVo = backendCategoryBizService.parentAndSelfDetail(reqVo.getCategoryId());
        return Result.success(respVo);
    }

    @PostMapping("options")
    public Result<List<BackendCategoryOptionRespVo>> options() {
        List<BackendCategory> categories = backendCategoryService.list();
        List<BackendCategoryOptionRespVo> options = categories.stream().map(category -> {
            BackendCategoryOptionRespVo option = new BackendCategoryOptionRespVo();
            option.setCategoryId(category.getCategoryId());
            option.setCategoryName(category.getCategoryName());
            option.setParentId(category.getParentId());
            option.setLevel(category.getLevel());
            return option;
        }).collect(Collectors.toList());
        return Result.success(options);
    }

    @PostMapping("queryAttrSaleList")
    public Result<Object> queryAttrSaleList(@RequestBody BackendCategoryAttrReqVo reqVo) {
        BackendCategory backendCategory = backendCategoryService.getOne(
                new LambdaQueryWrapper<BackendCategory>()
                        .eq(BackendCategory::getCategoryId, reqVo.getCategoryId()));
        if (Objects.isNull(backendCategory)) {
            throw new BizException("??????????????????????????????");
        }
        List<CategoryPropertySale> propertySaleList = categoryPropertySaleService.list(
                new LambdaQueryWrapper<CategoryPropertySale>()
                        .eq(CategoryPropertySale::getCategoryId, reqVo.getCategoryId()));
        List<PropertySaleKey> propertySaleKeys = new ArrayList<>();
        if (!CollectionUtils.isEmpty(propertySaleList)) {
            List<String> attrIds = propertySaleList.stream().map(CategoryPropertySale::getPropertySaleId)
                    .collect(Collectors.toList());
            propertySaleKeys = propertySaleBizService.getListAndRelatedByPropertyIds(attrIds);
        }
        return Result.success(propertySaleKeys);
    }

    @PostMapping("addAttrSale")
    public Result<Object> addAttrSale(@RequestBody BackendCategoryAttrReqVo reqVo) {
        BackendCategory backendCategory = backendCategoryService.getOne(
                new LambdaQueryWrapper<BackendCategory>()
                        .eq(BackendCategory::getCategoryId, reqVo.getCategoryId()));
        if (Objects.isNull(backendCategory)) {
            throw new BizException("??????????????????????????????");
        }
        PropertySaleKey propertySaleKey = propertySaleKeyService.getOne(
                new LambdaQueryWrapper<PropertySaleKey>()
                        .eq(PropertySaleKey::getKeyId, reqVo.getAttrId()));
        if (Objects.isNull(propertySaleKey)) {
            throw new BizException("??????????????????????????????");
        }
        CategoryPropertySale categoryPropertySale = categoryPropertySaleService.getOne(
                new LambdaQueryWrapper<CategoryPropertySale>()
                        .eq(CategoryPropertySale::getCategoryId, reqVo.getCategoryId())
                        .eq(CategoryPropertySale::getPropertySaleId, reqVo.getAttrId()));
        if (Objects.nonNull(categoryPropertySale)) {
            throw new BizException("????????????????????????");
        }
        CategoryPropertySale save = new CategoryPropertySale();
        save.setCategoryId(reqVo.getCategoryId());
        save.setPropertySaleId(reqVo.getAttrId());
        save.setOrderNo(0);
        save.setStatus(StatusEnum.ENABLED.code);
        categoryPropertySaleService.save(save);
        return Result.success();
    }

    @PostMapping("deleteAttrSale")
    public Result<Object> deleteAttrSale(@RequestBody BackendCategoryAttrReqVo reqVo) {
        categoryPropertySaleService.remove(
                new LambdaQueryWrapper<CategoryPropertySale>()
                        .eq(CategoryPropertySale::getCategoryId, reqVo.getCategoryId())
                        .eq(CategoryPropertySale::getPropertySaleId, reqVo.getAttrId()));
        return Result.success();
    }
}
