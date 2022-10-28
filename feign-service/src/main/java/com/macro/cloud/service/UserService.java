package com.macro.cloud.service;

import com.macro.cloud.domain.CommonResult;
import com.macro.cloud.domain.User;
import com.macro.cloud.service.impl.UserFallbackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 添加UserService接口完成对 user-service 服务的接口绑定
 * UserFallbackService 服务降级处理类，需要注意的是它实现了UserService接口，并且对接口中的每个实现方法进行了服务降级逻辑的实现。
 *
 * @FeignClient 一般采用服务名进行命名
 * name：指定FeignClient的名称，如果项目使用了Ribbon，name属性会作为微服务的名称，用于服务发现
 * value: value和 name的作用一样，如果没有配置url那么配置的值将作为服务名称，用于服务发现。反之只是一个名称。
 * serviceId: serviceId已经废弃了，直接使用name即可。
 * contextId: 比如我们有个user服务，但user服务中有很多个接口，我们不想将所有的调用接口都定义在一个类中,每个接口上都加了@FeignClient(name = "user")注解，
 *            这种情况下启动就会报错了，因为Bean的名称冲突了。一种解决方案就是为每个Client手动指定不同的 contextId，这样就不会冲突了。
 *            如果配置了contextId就会用contextId，如果没有配置就会去value然后是name最后是serviceId。默认都没有配置，当出现一个服务有多个Feign Client的时候就会报错了。
 * url: url用于配置指定服务的地址，相当于直接请求这个服务，不经过Ribbon的服务选择。像调试等场景可以使用, 可以手动指定@FeignClient调用的地址
 * configuration：Feign 配置类，可以自定义 Feign 的 Encoder、Decoder、LogLevel、Contract。
 * fallback：定义容错的处理类，当调用远程接口失败或超时时，会调用对应接口的容错逻辑，fallback 指定的类必须实现 @FeignClient 标记的接口。
 * fallbackFactory：工厂类，用于生成 fallback 类示例，通过这个属性我们可以实现每个接口通用的容错逻辑，减少重复的代码。
 * path: path定义当前FeignClient访问接口时的统一前缀，比如接口地址是/user/get, 如果你定义了前缀是user, 那么具体方法上的路径就只需要写/get 即可。
 *
 * Created by macro on 2019/9/5.
 */
@FeignClient(value = "user-service", fallback = UserFallbackService.class)
public interface UserService {
    @PostMapping("/user/create")
    CommonResult create(@RequestBody User user);

    @GetMapping("/user/{id}")
    CommonResult<User> getUser(@PathVariable Long id);

    @GetMapping("/user/getByUsername")
    CommonResult<User> getByUsername(@RequestParam String username);

    @PostMapping("/user/update")
    CommonResult update(@RequestBody User user);

    @PostMapping("/user/delete/{id}")
    CommonResult delete(@PathVariable Long id);
}
