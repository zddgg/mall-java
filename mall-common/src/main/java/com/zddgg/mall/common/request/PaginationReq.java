package com.zddgg.mall.common.request;

import lombok.Data;

@Data
public class PaginationReq {

    private Long current = 1L;

    private Long pageSize = 10L;
}
