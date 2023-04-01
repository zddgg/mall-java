package com.zddgg.mall.oms.vo.resp;

import lombok.Data;

import java.util.List;

@Data
public class RouteRecordRespVO {

    private String path;

    private String name;

    private RouteMeta meta;

    private List<RouteRecordRespVO> children;

    @Data
    public static class RouteMeta {

        private String locale;

        private Boolean requiresAuth;

        private String icon;

        private Integer order;
    }
}
