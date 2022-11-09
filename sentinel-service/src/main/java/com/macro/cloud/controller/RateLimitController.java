package com.macro.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.macro.cloud.domain.CommonResult;
import com.macro.cloud.handler.CustomBlockHandler;
import com.macro.cloud.util.ExceptionUtil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 限流功能
 * Created by macro on 2019/11/7.
 */
@RestController
@RequestMapping("/rateLimit")
public class RateLimitController {

    /**
     * 根据 @SentinelResource注解中定义的value（资源名称）来进行限流操作，需要指定限流处理逻辑
     *
     * 如果配置 blockHandler 但是没有配置 blockHandlerClass，那么取当前执行方法的类为待执行类。
     *
     */
    @GetMapping("/byResource")
    @SentinelResource(value = "byResource",blockHandler = "handleException")
    public CommonResult byResource() {
        return new CommonResult("按资源名称限流", 200);
    }

    /**
     * 按URL限流，有默认的限流处理逻辑。资源名称：/rateLimit/byUrl
     * 限流默认处理类 DefaultBlockExceptionHandler
     */
    @GetMapping("/byUrl")
    @SentinelResource(value = "byUrl", blockHandler = "handleException")
    public CommonResult byUrl() {
        return new CommonResult("按url限流", 200);
    }

    /**
     * 自定义通用的限流处理逻辑
     *
     * blockHandler必须有值，如果没配置，那么不执行 blockHandler，
     * 如果配置 blockHandler 也配置了blockHandlerClass，那么会从 blockHandlerClass 数组的第一个元素作为待执行的类；
     */
    @GetMapping("/customBlockHandler")
    @SentinelResource(value = "customBlockHandler", blockHandler = "handleException", blockHandlerClass = CustomBlockHandler.class)
    public CommonResult blockHandler() {
        return new CommonResult("限流成功", 200);
    }

    @GetMapping("/customBlockHandler2")
    @SentinelResource(value = "customBlockHandler2", blockHandler = "jsonHandleException", blockHandlerClass = ExceptionUtil.class)
    public CommonResult blockHandler2() {
        return new CommonResult("限流成功", 200);
    }

    /**
     * blockHandler 处理方法在当前类
     *
     * @param exception
     * @return
     */
    public CommonResult handleException(BlockException exception){
        System.out.println("----->>>>>> blockHandler 处理方法是当前类: " + exception.getClass().getCanonicalName());
        return new CommonResult(exception.getClass().getCanonicalName(),200);
    }

    //使用 @SentinelResource 注解实现自定义限流处理逻辑:
    //1、blockHandler必须有值，如果没配置，那么不执行 blockHandler，如果配置 blockHandler 也配置了blockHandlerClass，那么会从 blockHandlerClass 数组的第一个元素作为待执行的类；
    //
    //2、如果配置 blockHandler 但是没有配置 blockHandlerClass，那么取当前执行方法的类为待执行类。
    //
    //3、如果待执行类不是当前类，那么方法必须是一个静态方法，且方法名与 blockHandler 配置一致；如果待执行类的是当前类，那么方法静态或非静态方法都可以；
    //
    //4、方法的返回值必须与 SentinelResource 注解的 blockHandler 方法返回值一样；
    //
    //5、参数方面， blockHandler 方法比 SentinelResource 注解的方法的参数多一个，最后一个参数必须是 BlockException 类型，前面的参数必须与 SentinelResource 注解的方法参数完全一致。

}
