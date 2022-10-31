package com.macro.cloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by macro on 2019/8/29.
 */
@Configuration
public class RibbonConfig {

    /**
     * 添加了@LoadBalanced注解之后，Ribbon会给restTemplate请求添加一个拦截器，
     * 在拦截器中获取 注册中心的服务列表，并使用 Ribbon内置的负载均衡算法从服务列表里选中一个服务，默认是轮询策略
     * 通过获取到的服务信息 （ip,port）替换 serviceId 实现负载请求。
     *
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
