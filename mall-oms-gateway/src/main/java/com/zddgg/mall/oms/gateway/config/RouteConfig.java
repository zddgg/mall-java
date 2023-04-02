package com.zddgg.mall.oms.gateway.config;

import com.zddgg.mall.oms.gateway.filter.OmsTokenCheckFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
public class RouteConfig {

    private final StringRedisTemplate stringRedisTemplate;

    @Bean
    public RouteLocator customizeRoute(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(
                        "mall-product-route",
                        r -> r.path("/api/product/**")
                                .filters(f -> f.stripPrefix(1)
                                        .filter(new OmsTokenCheckFilter(stringRedisTemplate)))
                                .uri("lb://mall-product")
                )
                .route(
                        "mall-file-route",
                        r -> r.path("/api/file/**")
                                .filters(f -> f.stripPrefix(1)
                                        .filter(new OmsTokenCheckFilter(stringRedisTemplate)))
                                .uri("lb://mall-file")
                )
                .route(
                        "mall-oms-route",
                        r -> r.path("/api/oms/**")
                                .filters(f -> f.stripPrefix(1)
                                        .filter(new OmsTokenCheckFilter(stringRedisTemplate)))
                                .uri("lb://mall-oms")
                )
                .build();
    }
}