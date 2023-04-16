package com.zddgg.mall.product.controller.app;

import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.BackendCategoryNode;
import com.zddgg.mall.product.biz.BackendCategoryBizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {

    private final BackendCategoryBizService categoryBizService;

    @PostMapping("tree")
    public Result<List<BackendCategoryNode>> tree() {
        return Result.success(categoryBizService.getRootList(3));
    }
}
