package com.macro.cloud.util;


import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.fastjson.JSONObject;
import com.macro.cloud.domain.CommonResult;

public class ExceptionUtil {

    public static CommonResult jsonHandleException(BlockException exception) {
        System.out.println("----->>>>>> blockHandler 处理方法非当前类: " + exception.getClass().getCanonicalName());
        return new CommonResult(exception.getClass().getCanonicalName(),200);
    }

    public static JSONObject calculateHandleException(String a, String b, String type, BlockException ex) {
        JSONObject jsonObject = new JSONObject();
        String msg="";
        if (ex instanceof FlowException) {
            msg = "限流了";
        } else if (ex instanceof ParamFlowException) {
            msg = "热点参数限流";
        } else if (ex instanceof DegradeException) {
            msg = "降级了";
        }
        jsonObject.put("msg", msg);
        jsonObject.put("异常处理方法", "com.macro.cloud.util.ExceptionUtil.calculateHandleException()");
        jsonObject.put("exception", ex.getClass().getCanonicalName());
        return jsonObject;
    }

}