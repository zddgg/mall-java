package com.zddgg.mall.app.gateway.filter;

import com.alibaba.fastjson2.JSON;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class UserTokenCheckFilter implements GatewayFilter, Ordered {

    static List<String> authList = new ArrayList<>();

    static {
        authList.add("/oms/user/login");
        authList.add("/oms/user/logout");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        ServerHttpRequest request = exchange.getRequest();
        String first = request.getHeaders().getFirst("authorization");
        String userId;
        if (StringUtils.isNotBlank(first) && first.split(" ").length == 2) {
            String tokenStr = first.split(" ")[1];
            if (JwtUtil.verifyToken(tokenStr) == 0) {
                Map<String, Object> map = JwtUtil.parseToken(tokenStr);
                userId = (String) map.get("userId");
                String token = (String) map.get("token");
            } else {
                userId = null;
            }
        } else {
            userId = null;
        }
        Consumer<HttpHeaders> headersConsumer = httpHeaders -> {
            httpHeaders.set("user-id", userId);
        };

        if (!authList.contains(path)) {
            return chain.filter(exchange.mutate().request(exchange.getRequest().mutate().headers(headersConsumer).build()).build());
        }
        ServerHttpResponse response = exchange.getResponse();
        Result<Object> result = Result.fail("50008", "无效token");
        return response.writeWith(Flux.just(response.bufferFactory().wrap(JSON.toJSONBytes(result))));
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
