package com.zddgg.mall.product.biz;

import com.zddgg.mall.common.response.PaginationRes;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.product.bean.BackendCategoryDetail;
import com.zddgg.mall.product.bean.BackendCategoryListVo;
import com.zddgg.mall.product.bean.BackendCategoryNode;
import com.zddgg.mall.product.bean.category.backend.req.BackendCategoryCreateReqVo;
import com.zddgg.mall.product.bean.category.backend.req.BackendCategoryUpdateReqVo;

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

    void update(BackendCategoryUpdateReqVo reqVo);

    Result<PaginationRes<BackendCategoryNode>> pageList(BackendCategoryListVo vo);
}
