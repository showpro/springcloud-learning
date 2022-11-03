package com.macro.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by macro on 2019/9/11.
 */
@RestController
@RefreshScope //通过@Value注解获取的注册中心的属性值，需要通过该注解动态感知修改
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;
    @Value("${configdata.age}")
    private String age;
    @Value("${people.name}")
    private String people;
    @Autowired
    private UserConfig userConfig;

    /**
     * 从Nacos配置中心中获取配置信息
     *
     * @return
     */
    @GetMapping("/configInfo")
    public String getConfigInfo() {
        return configInfo;
    }

    /**
     * application-common.yml
     *
     * configdata:
     *     name: zhangsan123
     *     age: 18
     *
     * @return
     */
    @GetMapping("/configdata")
    public String getConfigdata() {
        return userConfig.toString();
    }

    /**
     * application-custom.yml
     *
     * people:
     *     name: wangwu
     *
     * @return
     */
    @GetMapping("/people")
    public String getPeople() {
        return people;
    }
}
