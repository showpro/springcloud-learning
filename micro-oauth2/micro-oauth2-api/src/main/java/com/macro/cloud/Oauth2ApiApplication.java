package com.macro.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 受保护的API服务，网关层用户鉴权通过后可以访问该服务
 */
@EnableDiscoveryClient
@SpringBootApplication
public class Oauth2ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ApiApplication.class, args);
    }

}
