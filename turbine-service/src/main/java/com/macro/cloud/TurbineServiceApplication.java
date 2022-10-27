package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * 这里我们使用Turbine来聚合hystrix-service服务的监控信息，然后我们的 hystrix-dashboard服务
 * 就可以从Turbine获取聚合好的监控信息展示给我们了。
 */
@EnableTurbine  //启用Turbine相关功能
@EnableDiscoveryClient
@SpringBootApplication
public class TurbineServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TurbineServiceApplication.class, args);
    }

}
