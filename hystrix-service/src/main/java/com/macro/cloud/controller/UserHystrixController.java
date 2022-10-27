package com.macro.cloud.controller;

import cn.hutool.core.thread.ThreadUtil;

import com.macro.cloud.domain.CommonResult;
import com.macro.cloud.domain.User;
import com.macro.cloud.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 创建UserHystrixController接口用于调用user-service服务
 * Created by macro on 2019/9/3.
 */
@RestController
@RequestMapping("/user")
public class UserHystrixController {
    @Autowired
    private UserService userService;

    /**
     * 用于测试服务降级
     * http://localhost:8401/user/testFallback/1
     *
     * @param id
     * @return
     */
    @GetMapping("/testFallback/{id}")
    public CommonResult testFallback(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/testCommand/{id}")
    public CommonResult testCommand(@PathVariable Long id) {
        return userService.getUserCommand(id);
    }

    /**
     * 抛异常降级
     *
     * @param id
     * @return
     */
    @GetMapping("/testException/{id}")
    public CommonResult testException(@PathVariable Long id) {
        return userService.getUserException(id);
    }

    /**
     * 测试使用缓存  http://localhost:8401/user/testCache/1
     * 直接调用三次getUserCache方法, 但是只打印了一次日志，说明有两次走的是缓存：
     *
     * @param id
     * @return
     */
    @GetMapping("/testCache/{id}")
    public CommonResult testCache(@PathVariable Long id) {
        userService.getUserCache(id);
        userService.getUserCache(id);
        userService.getUserCache(id);
        return new CommonResult("操作成功", 200);
    }

    /**
     * 移除缓存   http://localhost:8401/user/testRemoveCache/1
     * 可以发现有两次查询都走的是接口：
     *
     * @param id
     * @return
     */
    @GetMapping("/testRemoveCache/{id}")
    public CommonResult testRemoveCache(@PathVariable Long id) {
        userService.getUserCache(id);
        userService.removeCache(id);
        userService.getUserCache(id);
        return new CommonResult("操作成功", 200);
    }

    /**
     * 对同一个服务的请求合并
     * 这里我们先进行两次服务调用，再间隔200ms以后进行第三次服务调用：
     *
     * 访问接口测试 http://localhost:8401/user/testCollapser
     * 由于我们设置了100毫秒进行一次请求合并，前两次被合并，最后一次自己单独合并了。
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/testCollapser")
    public CommonResult testCollapser() throws ExecutionException, InterruptedException {
        Future<User> future1 = userService.getUserFuture(1L);
        Future<User> future2 = userService.getUserFuture(2L);
        future1.get();
        future2.get();
        ThreadUtil.safeSleep(200);
        Future<User> future3 = userService.getUserFuture(3L);
        future3.get();
        return new CommonResult("操作成功", 200);
    }
}
