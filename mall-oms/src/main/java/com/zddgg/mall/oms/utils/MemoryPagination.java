package com.zddgg.mall.oms.utils;

import com.zddgg.mall.common.response.PaginationRes;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemoryPagination {

    /**
     * 内存分页
     *
     * @param data     待分页的数据
     * @param current  当前页码
     * @param pageSize 每页显示的条数
     * @return 分页之后的数据
     */
    public static <T> PaginationRes<T> page(List<T> data, long current, long pageSize) {
        PaginationRes<T> page = new PaginationRes<>();
        if (CollectionUtils.isEmpty(data)) {
            page.setTotal(0L);
            page.setCurrent(current);
            page.setPageSize(pageSize);
            page.setRecords(new ArrayList<>());
            return page;
        }
        int totalCount = data.size();
        int maxPage = (int) Math.ceil((double) totalCount / (double) pageSize);
        if (current > maxPage) {
            current = maxPage;
        }
        List<T> records = data.stream().skip((current - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        page.setTotal((long) totalCount);
        page.setCurrent(current);
        page.setPageSize(pageSize);
        page.setRecords(records);
        return page;
    }
}
