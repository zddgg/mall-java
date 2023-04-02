package com.zddgg.mall.oms.gateway.config;

import com.zddgg.mall.oms.gateway.filter.OmsTokenCheckFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customizeRoute(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(
                        "mall-product-route",
                        r -> r.path("/api/product/**")
                                .filters(f -> f.stripPrefix(1)
                                        .filter(new OmsTokenCheckFilter()))
                                .uri("lb://mall-product")
                )
                .route(
                        "mall-file-route",
                        r -> r.path("/api/file/**")
                                .filters(f -> f.stripPrefix(1)
                                        .filter(new OmsTokenCheckFilter()))
                                .uri("lb://mall-file")
                )
                .route(
                        "mall-oms-route",
                        r -> r.path("/api/oms/**")
                                .filters(f -> f.stripPrefix(1)
                                        .filter(new OmsTokenCheckFilter()))
                                .uri("lb://mall-oms")
                )
                .build();
    }
}