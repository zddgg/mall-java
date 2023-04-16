package com.zddgg.mall.app.gateway.config;

import com.zddgg.mall.app.gateway.filter.UserTokenCheckFilter;
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
                        "mall-user-route",
                        r -> r.path("/api/app/user/**")
                                .filters(f -> f.stripPrefix(2)
                                        .filter(new UserTokenCheckFilter()))
                                .uri("lb://mall-user")
                )
                .route(
                        "mall-product-route",
                        r -> r.path("/api/app/product/**")
                                .filters(f -> f.stripPrefix(2)
                                        .filter(new UserTokenCheckFilter()))
                                .uri("lb://mall-product")
                )
                .route(
                        "mall-cart-route",
                        r -> r.path("/api/app/cart/**")
                                .filters(f -> f.stripPrefix(2)
                                        .filter(new UserTokenCheckFilter()))
                                .uri("lb://mall-cart")
                )
                .build();
    }
}