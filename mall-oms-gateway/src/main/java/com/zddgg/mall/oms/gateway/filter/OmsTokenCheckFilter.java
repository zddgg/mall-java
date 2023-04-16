package com.zddgg.mall.oms.gateway.filter;

import com.alibaba.fastjson2.JSON;
import com.zddgg.mall.common.response.Result;
import com.zddgg.mall.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OmsTokenCheckFilter implements GatewayFilter, Ordered {

    private final StringRedisTemplate stringRedisTemplate;

    static List<String> authIgnoreList = new ArrayList<>();

    static {
        authIgnoreList.add("/oms/user/login");
        authIgnoreList.add("/oms/user/logout");
    }

    public OmsTokenCheckFilter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        ServerHttpRequest request = exchange.getRequest();
        if (authIgnoreList.contains(path)) {
            return chain.filter(exchange);
        }
        String first = request.getHeaders().getFirst("authorization");
        if (StringUtils.isNotBlank(first) && first.split(" ").length == 2) {
            String tokenStr = first.split(" ")[1];
            if (JwtUtil.verifyToken(tokenStr) == 0) {
                Map<String, Object> map = JwtUtil.parseToken(tokenStr);
                String userId = (String) map.get("userId");
                String token = (String) map.get("token");
                String redisToken = stringRedisTemplate.opsForValue().get("oms-user:" + userId + ":token");
                if (StringUtils.equals(token, redisToken)) {
                    // token 过期时间重置
                    stringRedisTemplate.opsForValue().set("oms-user:" + userId + ":token", token, 30, TimeUnit.MINUTES);
                    return chain.filter(exchange);
                }
            }
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
