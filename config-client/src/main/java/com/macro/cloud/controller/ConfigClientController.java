package com.macro.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by macro on 2019/9/11.
 */
@RestController
@RefreshScope // 该注解用于刷新配置。  通过访问POST请求刷新git的配置：curl -X POST “http://localhost:9001/actuator/refresh”
public class ConfigClientController {

    // 因为config仓库以rest形式暴露，所以所有客户端都可以通过config服务端访问到git上对应的文件信息
    @Value("${config.info}")
    private String configInfo;

    /**
     * http://localhost:9001/configInfo
     * 结果：config info for dev(dev)
     *
     * @return
     */
    @GetMapping("/configInfo")
    public String getConfigInfo() {
        return configInfo;
    }
}
