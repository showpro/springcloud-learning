package com.macro.cloud.util;

import com.alibaba.fastjson.JSONObject;

public class FallbackUtil {

    public static JSONObject calculateFallback(String a, String b, String type, Throwable throwable) {
        JSONObject jsonObject = new JSONObject();
        if (throwable instanceof ArithmeticException) {
            jsonObject.put("fallback", "运算异常，calculateFallback");
            jsonObject.put("throwable", throwable.getClass().getCanonicalName());
            jsonObject.put("message", throwable.getMessage());
        }
        return jsonObject;
    }
}
