package com.macro.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.macro.cloud.util.ExceptionUtil;
import com.macro.cloud.util.FallbackUtil;

/**
 * 限流 和 熔断降级 同时满足
 */
@RestController
@RequestMapping("/hystrix")
public class HystrixController {

	/*
	* 如果代码抛出 BlockException 异常则执行 ExceptionUtil 类的 calculateHandleException 方法并 return
	* 如果代码未抛出  BlockException 异常则执行 FallbackUtil 类的 calculateFallback 方法并退出
	*
	* 测试接口： http://localhost:8401/hystrix/calculate?a=10&b=0&type=/
	* 从传递参数看出 10/0 会抛出算术异常
	* QPS 等于 1 走 fallback
	* QPS 大于 1，走 blockHandler， 限流和熔断降级同时满足，优先限流
	*/
    @GetMapping("/calculate")
    @SentinelResource(value = "calculate", blockHandler = "calculateHandleException",blockHandlerClass = ExceptionUtil.class, fallbackClass = FallbackUtil.class, fallback = "calculateFallback")
    public JSONObject calculate(String a, String b, String type) {
        int result = 0;
        int num1 = Integer.parseInt(a);
        int num2 = Integer.parseInt(b);
        switch (type) {
            case "+":result = num1 + num2;break;
            case "-":result = num1 - num2;break;
            case "*":result = num1 * num2;break;
            case "/":result = num1 / num2;break;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result",result);
        return jsonObject;
    }
}
