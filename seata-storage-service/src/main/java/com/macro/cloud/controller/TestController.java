package com.macro.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanzhan
 * @date 2022/11/2 22:24
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Value("${activate}")
    private String activate;

    @Value("${name}")
    private String name;

    @GetMapping("/public-test")
    public String test() {
        return "当前环境为：" + activate + "：" + name;
    }
}
