package com.macro.cloud.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.macro.cloud.domain.CommonResult;

/**
 * 自定义限流处理逻辑
 * 方式二：创建 CustomBlockHandler 类用于自定义限流处理逻辑，
 * 然后使用 @SentinelResource 注解的 blockHandler 和 blockHandlerClass 两个参数指定限流处理逻辑方法
 * Created by macro on 2019/11/7.
 */
public class CustomBlockHandler {

    public CommonResult handleException(BlockException exception){
        return new CommonResult("自定义限流信息",200);
    }
}
