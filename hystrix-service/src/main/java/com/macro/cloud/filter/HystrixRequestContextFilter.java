package com.macro.cloud.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

import java.io.IOException;

/**
 * 在缓存使用过程中，我们需要在每次使用缓存的请求前后对 HystrixRequestContext 进行初始化和关闭，否则会出现异常：
 * 通过使用过滤器，在每个请求前后初始化和关闭 HystrixRequestContext 来解决该问题
 *
 * Created by macro on 2019/9/4.
 */
@Component
@WebFilter(urlPatterns = "/*", asyncSupported = true)
public class HystrixRequestContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("--->>> 使用缓存请求前对 HystrixRequestContext 进行初始化");
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            System.out.println("--->>> 使用缓存请求后对 HystrixRequestContext 进行关闭");
            context.close();
        }
    }
}
