package com.macro.cloud.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 启动 oauth2-client服务 和 oauth2-jwt-server服务
 * 访问客户端需要授权的接口 http://localhost:9501/user/getCurrentUser  会跳转到授权服务的登录界面；http://localhost:9401/login
 * Created by macro on 2019/9/30.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 获取当前登录用户信息
     *
     * @param authentication
     * @return
     */
    @GetMapping("/getCurrentUser")
    public Object getCurrentUser(Authentication authentication) {
        return authentication;
    }

    /**
     * 需要admin权限的接口
     * 
     * 使用没有admin权限的帐号，比如zhangsan:123456获取令牌后访问该接口
     *
     * @return
     */
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/auth/admin")
    public Object adminAuth() {
        return "Has admin auth!";
    }

}
