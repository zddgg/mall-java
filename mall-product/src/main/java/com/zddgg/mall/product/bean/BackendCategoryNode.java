package com.zddgg.mall.product.bean;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BackendCategoryNode {

    private Long id;

    private String creator;

    private LocalDateTime created;

    private String updater;

    private LocalDateTime updated;

    private String categoryId;

    private String categoryName;

    private String parentId;

    private Integer level;

    private String status;

    private Integer groupCount;

    private Integer storeCount;

    private Integer saleCount;

    private List<BackendCategoryNode> children;
}
