package com.zddgg.mall.oms.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class OmsGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(OmsGatewayApplication.class, args);
    }
}
