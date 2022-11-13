package com.macro.cloud.filter;

import com.macro.cloud.config.IgnoreUrlsConfig;
import com.macro.cloud.constant.AuthConstant;
import com.nimbusds.jose.shaded.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * 白名单路径访问时需要移除JWT请求头
 * Created by macro on 2020/7/24.
 */
@Component
public class IgnoreUrlsRemoveJwtFilter implements WebFilter {
    static Logger logger = LoggerFactory.getLogger(IgnoreUrlsRemoveJwtFilter.class);
    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    /**
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        PathMatcher pathMatcher = new AntPathMatcher();
        //白名单路径移除JWT请求头
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, uri.getPath())) {
                //向 headers中放文件,name:Authorization , value:""，记得build
                request = exchange.getRequest().mutate().header("Authorization", "").build();
                //将现在的 request 变成 change对象。这样发送给路由的request中就放了我们自定义的数据了
                exchange = exchange.mutate().request(request).build();
                return chain.filter(exchange);
            }
        }
        return chain.filter(exchange);
    }
}
