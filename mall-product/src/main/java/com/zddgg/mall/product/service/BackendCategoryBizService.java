package com.zddgg.mall.product.service;

import com.zddgg.mall.common.response.PaginationRes;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.BackendCategoryCreateReqVo;
import com.zddgg.mall.product.bean.BackendCategoryDetailRespVo;
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

    BackendCategoryDetailRespVo detail(String parentCategoryNo);

    List<BackendCategoryDetailRespVo> allParentDetail(String parentCategoryNo);

    void update(BackendCategoryCreateReqVo reqVo);

    Result<PaginationRes<BackendCategoryNode>> pageList(BackendCategoryListVo vo);
}
