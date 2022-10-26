package com.macro.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 使用Consul作为配置中心
 * Created by macro on 2019/9/11.
 */
@RestController
@RefreshScope
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;

    /**
     * 从Consul配置中心中获取配置信息
     * 在consul中添加配置存储的key为:  config/consul-config-client:dev/data
     * 在consul中添加配置存储的value为：
     *  config:
     *   info: "config info for dev"
     *
     * @return
     */
    @GetMapping("/configInfo")
    public String getConfigInfo() {
        return configInfo;
    }
}
