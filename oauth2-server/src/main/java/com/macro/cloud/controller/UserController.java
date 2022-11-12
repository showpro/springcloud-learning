package com.macro.cloud.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 当前用户登录信息测试
 *
 * Created by macro on 2019/9/30.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    // 1、启动oauth2-server服务；
    // 2、在浏览器访问该地址进行登录授权：http://localhost:9401/oauth/authorize?response_type=code&client_id=admin&redirect_uri=http://www.baidu.com&scope=all&state=normal
    // 3、输入账号密码进行登录操作：zhan 123456
    // 之后会浏览器会带着授权码重定向跳转到我们指定的路径：https://www.baidu.com/?code=rKhK5F&state=normal 。它会重定向到URL，如：http://localhost:9401/login?code=TUXuk9 。这里'TUXuk9'是第三方应用程序的授权代码。
    // 4、应用程序将使用返回的授权码请求该地址获取访问令牌,如下：
    // curl --location --request POST 'http://localhost:9401/oauth/token' \
    // --header 'Authorization: Basic YWRtaW46YWRtaW4xMjM0NTY=' \
    // --header 'Content-Type: application/x-www-form-urlencoded' \
    // --data-urlencode 'grant_type=authorization_code' \
    // --data-urlencode 'code=EOUv7O' \
    // --data-urlencode 'client_id=admin' \
    // --data-urlencode 'redirect_uri=http://www.baidu.com' \
    // --data-urlencode 'scope=all'
    // 5、通过获取道德令牌访问 http://localhost:9401/user/getCurrentUser
    @GetMapping("/getCurrentUser")
    public Object getCurrentUser(Authentication authentication) {
        Object principal = SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
        return authentication.getPrincipal();
    }
}
