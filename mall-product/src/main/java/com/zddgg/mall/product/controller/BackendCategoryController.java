package com.zddgg.mall.product.controller;

import com.zddgg.mall.common.response.PaginationRes;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.*;
import com.zddgg.mall.product.entity.BackendCategory;
import com.zddgg.mall.product.service.BackendCategoryBizService;
import com.zddgg.mall.product.service.BackendCategoryService;
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
public class BackendCategoryController {

    private final BackendCategoryService backendCategoryService;

    private final BackendCategoryBizService backendCategoryBizService;

    public BackendCategoryController(BackendCategoryService backendCategoryService,
                                     BackendCategoryBizService backendCategoryBizService) {
        this.backendCategoryService = backendCategoryService;
        this.backendCategoryBizService = backendCategoryBizService;
    }

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
            root.setCategoryName("根类目");
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
    public Result<BackendCategoryDetailRespVo> detail(@RequestBody BackendCategoryDetailReqVo reqVo) {
        BackendCategoryDetailRespVo respVo = backendCategoryBizService.detail(reqVo.getCategoryId());
        return Result.success(respVo);
    }

    @PostMapping("allParentDetail")
    public Result<List<BackendCategoryDetailRespVo>> allParentDetail(@RequestBody BackendCategoryDetailReqVo reqVo) {
        List<BackendCategoryDetailRespVo> respVo = backendCategoryBizService.allParentDetail(reqVo.getCategoryName());
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
}
