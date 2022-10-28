package com.macro.cloud.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * 添加限流策略的配置类
 * Created by macro on 2019/9/25.
 */
@Configuration
public class RedisRateLimiterConfig {

    //server:
    //  port: 9201
    //spring:
    //  redis:
    //    host: localhost
    //    port: 6379
    //  cloud:
    //    gateway:
    //      routes:
    //        - id: requestratelimiter_route
    //          uri: http://localhost:8201
    //          filters:
    //            - name: RequestRateLimiter # 过滤器名称
    //              args:
    //                redis-rate-limiter.replenishRate: 1 #每秒允许处理的请求数量
    //                redis-rate-limiter.burstCapacity: 2 #每秒最大处理的请求数量
    //                key-resolver: "#{@ipKeyResolver}" #限流策略，对应策略的Bean
    //          predicates:
    //            - Method=GET

    // 多次请求该地址：http://localhost:9201/user/1 ，会返回状态码为429的错误；

    /**
     * 一种是根据请求参数中的 username 进行限流
     *
     * @return
     */
//    @Bean
//    KeyResolver userKeyResolver() {
//        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("username"));
//    }

    /**
     * 另一种是根据访问IP进行限流
     *
     * @return
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }
}
