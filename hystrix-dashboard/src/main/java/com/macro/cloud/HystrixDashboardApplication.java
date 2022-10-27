package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * Hystrix提供了Hystrix Dashboard来实时监控 HystrixCommand方法的执行情况。
 * Hystrix Dashboard可以有效地反映出每个 Hystrix 实例的运行情况，帮助我们快速发现系统中的问题，从而采取对应措施
 *
 * 访问Hystrix Dashboard：http://localhost:8501/hystrix
 */
@EnableHystrixDashboard //启用监控功能
@EnableDiscoveryClient
@SpringBootApplication
public class HystrixDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApplication.class, args);
    }

}
