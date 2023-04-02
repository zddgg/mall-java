package com.zddgg.mall.oms.gateway.filter;

import com.zddgg.mall.oms.gateway.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class OmsTokenCheckFilter implements GatewayFilter, Ordered {

    static List<String> authIgnoreList = new ArrayList<>();

    static {
        authIgnoreList.add("/oms/user/login");
        authIgnoreList.add("/oms/user/logout");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        ServerHttpRequest request = exchange.getRequest();
        if (!authIgnoreList.contains(path)) {
            String first = request.getHeaders().getFirst("authorization");
            if (StringUtils.hasText(first) && first.split(" ").length == 2) {
                String tokenStr = first.split(" ")[1];
                if (JwtUtils.verifyToken(tokenStr) == 0) {
                    Map<String, Object> map = JwtUtils.parseToken(tokenStr);
                    String userId = (String) map.get("userId");
                    String token = (String) map.get("token");
                    System.out.println(userId);
                    System.out.println(token);
                }
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
