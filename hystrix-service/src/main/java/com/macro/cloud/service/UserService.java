package com.macro.cloud.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import com.macro.cloud.domain.CommonResult;
import com.macro.cloud.domain.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by macro on 2019/9/3.
 */
@Service
public class UserService {

    private Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private RestTemplate restTemplate;
    @Value("${service-url.user-service}")
    private String userServiceUrl;

    @HystrixCommand(fallbackMethod = "getDefaultUser")// 指定服务降级的处理方法名称
    public CommonResult getUser(Long id) {
        // 当前服务的方法中调用远程服务方法
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
    }

    public CommonResult getDefaultUser(@PathVariable Long id) {
        User defaultUser = new User(-1L, "defaultUser", "123456");
        return new CommonResult<>(defaultUser);
    }

    /**
     * 使用ignoreExceptions忽略某些异常降级
     *
     * ignoreExceptions  指定服务降级处理方法
     * fallbackMethod 忽略某些异常，不发生服务降级；
     *
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "getDefaultUser2", ignoreExceptions = {NullPointerException.class})
    public CommonResult getUserException(Long id) {
        if (id == 1) {
            // 这里抛异常导致服务降级，会调用 getDefaultUser2方法进行业务处理
            throw new IndexOutOfBoundsException();
        } else if (id == 2) {
            // 这里抛异常被忽略，服务未降级
            throw new NullPointerException();
        }
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
    }

    public CommonResult getDefaultUser2(@PathVariable Long id, Throwable e) {
        LOGGER.error("getDefaultUser2 id:{},throwable class:{}", id, e.getClass());
        User defaultUser = new User(-2L, "defaultUser2", "123456");
        return new CommonResult<>(defaultUser);
    }

    /**
     * 设置命令、分组及线程池名称
     *
     * commandKey 命令名称，用于区分不同的命令；
     * groupKey 分组名称，Hystrix会根据不同的分组来统计命令的告警及仪表盘信息；
     * threadPoolKey 线程池名称，用于划分线程池。
     *
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "getDefaultUser", commandKey = "getUserCommand", groupKey = "getUserGroup",
        threadPoolKey = "getUserThreadPool")
    public CommonResult getUserCommand(@PathVariable Long id) {
        LOGGER.info("getUserCommand id:{}", id);
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
    }

    /**
     * Hystrix的请求缓存
     *
     * @CacheResult 开启缓存，默认所有参数作为缓存的key，cacheKeyMethod可以通过返回String类型的方法指定key；
     * @CacheKey： 指定缓存的key，可以指定参数或指定参数中的属性值为缓存key，cacheKeyMethod还可以通过返回String类型的方法指定；
     *
     * @param id
     * @return
     */
    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(fallbackMethod = "getDefaultUser", commandKey = "getUserCache")
    public CommonResult getUserCache(Long id) {
        LOGGER.info("getUserCache id:{}", id);
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class, id);
    }

    /**
     * 为缓存生成key的方法
     */
    public String getCacheKey(Long id) {
        return String.valueOf(id);
    }

    /**
     * @CacheRemove： 移除缓存，需要指定commandKey。
     *
     * @param id
     * @return
     */
    @CacheRemove(commandKey = "getUserCache", cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public CommonResult removeCache(Long id) {
        LOGGER.info("removeCache id:{}", id);
        return restTemplate.postForObject(userServiceUrl + "/user/delete/{1}", null, CommonResult.class, id);
    }

    /**
     * 微服务系统中的服务间通信，需要通过远程调用来实现，随着调用次数越来越多，占用线程资源也会越来越多。
     * @HystrixCollapser 用于合并请求，从而达到减少通信消耗及线程数量的效果
     *
     * 使用@HystrixCollapser实现请求合并，所有对getUserFuture的的多次调用都会转化为对getUserByIds的单次调用：
     *
     * batchMethod：用于设置请求合并的方法；
     * collapserProperties：请求合并属性，用于控制实例属性，有很多；
     * timerDelayInMilliseconds：collapserProperties中的属性，用于控制多少毫秒内的请求会被合并成一个请求
     *
     * @param id
     * @return
     */
    @HystrixCollapser(batchMethod = "getUserByIds",
        collapserProperties = {@HystrixProperty(name = "timerDelayInMilliseconds", value = "100")})
    public Future<User> getUserFuture(Long id) {
        return new AsyncResult<User>() {
            @Override
            public User invoke() {
                CommonResult commonResult = restTemplate.getForObject(userServiceUrl + "/user/{1}", CommonResult.class,
                    id);
                Map data = (Map) commonResult.getData();
                User user = BeanUtil.mapToBean(data, User.class, true);
                LOGGER.info("getUserById username:{}", user.getUsername());
                return user;
            }
        };
    }

    @HystrixCommand
    public List<User> getUserByIds(List<Long> ids) {
        LOGGER.info("getUserByIds:{}", ids);
        CommonResult commonResult = restTemplate.getForObject(userServiceUrl + "/user/getUserByIds?ids={1}",
            CommonResult.class, CollUtil.join(ids, ","));
        return (List<User>) commonResult.getData();
    }
}
