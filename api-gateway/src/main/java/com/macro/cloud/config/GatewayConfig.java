package com.macro.cloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置路由方式一：使用yml配置
 * 配置路由方式二：使用Java Bean配置。添加相关配置类，并配置一个RouteLocator对象：
 * Created by macro on 2019/9/24.
 */
//@Configuration
public class GatewayConfig {

    /**
     * 测试地址：http://localhost:9201/user/getByUsername?username=macro
     *
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("path_route2", r -> r.path("/user/getByUsername")
                        .uri("http://localhost:8201/user/getByUsername"))
                .build();
    }
}
