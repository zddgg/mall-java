package com.zddgg.mall.common.response;

import lombok.Data;

import java.util.List;

@Data
public class PaginationRes<T> {

    private Long total;

    private Long current;

    private Long pageSize;

    private List<T> records;
}
