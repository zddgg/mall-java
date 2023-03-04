package com.zddgg.mall.product.bean;

import com.voidtime.mall.common.request.PaginationReq;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BackendCategoryListVo extends PaginationReq {

    private String categoryId;
}
