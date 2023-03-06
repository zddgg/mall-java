package com.zddgg.mall.product.service;

import com.zddgg.mall.common.response.PaginationRes;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.BackendCategoryCreateReqVo;
import com.zddgg.mall.product.bean.BackendCategoryDetail;
import com.zddgg.mall.product.bean.BackendCategoryListVo;
import com.zddgg.mall.product.bean.BackendCategoryNode;

import java.util.List;

public interface BackendCategoryBizService {

    List<BackendCategoryNode> getRootList(Integer maxLevel);

    BackendCategoryNode getNode(String backendCategoryNo);

    List<BackendCategoryNode> getSubNode(String backendCategoryNo);

    List<BackendCategoryNode> getSubNodeAndSelf(String backendCategoryNo);

    List<BackendCategoryNode> getAllParentNodes(String backendCategoryNo);

    List<BackendCategoryNode> getAllParentNodesAndSelf(String backendCategoryNo);

    void create(BackendCategoryCreateReqVo reqVo);

    BackendCategoryDetail detail(String categoryId);

    List<BackendCategoryDetail> parentDetail(String categoryId);

    List<BackendCategoryDetail> parentAndSelfDetail(String categoryId);

    void update(BackendCategoryCreateReqVo reqVo);

    Result<PaginationRes<BackendCategoryNode>> pageList(BackendCategoryListVo vo);
}
