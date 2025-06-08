package ynu.hmf.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ynu.hmf.entity.User;
import ynu.hmf.feign.UserFeignClient;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private UserFeignClient userFeignClient;

    @GetMapping("/getUser/{userId}")
    @CircuitBreaker(name = "circuitA", fallbackMethod = "getUserFallback")
    public User getUser(@PathVariable("userId") Integer userId) {
        return userFeignClient.getUserById(userId);
    }

    @PostMapping("/createUser")
    @CircuitBreaker(name = "circuitB", fallbackMethod = "createUserFallback")
    public User createUser(@RequestBody User user) {
        return userFeignClient.createUser(user);
    }



    @DeleteMapping("/deleteUser/{userId}")
    @RateLimiter(name = "backendD", fallbackMethod = "deleteUserFallback")
    public String deleteUser(@PathVariable("userId") Integer userId) {
        System.out.println("--- 正常调用 deleteUser ---");
        return userFeignClient.deleteUser(userId);
    }


    public User getUserFallback(Integer userId, Throwable throwable) {
        System.err.println("Fallback for getUserById, userId: " + userId + ", Error: " + throwable.getMessage());
        return new User(userId, "获取失败 (来自 CircuitA 的降级响应)", "用户服务当前不可用，请稍后重试");
    }

    public User createUserFallback(User user, Throwable throwable) {
        System.err.println("Fallback for createUser, user: " + user.getUserName() + ", Error: " + throwable.getMessage());
        return new User(0, "获取失败 (来自 CircuitB 的降级响应)", "用户服务当前不可用，请稍后重试");
    }

    public User updateUserFallback(Integer userId, User user, Throwable throwable) {
        System.err.println("Fallback for updateUser, userId: " + userId + ", Error: " + throwable.getMessage());
        return new User(userId,
              "更新失败 (来自 Bulkhead 的隔离响应)",
             "当前操作人数过多，请稍后重试");
    }
    public String deleteUserFallback(Integer userId, Throwable throwable) {
        System.err.println("!!! RateLimiter 'backendD' 触发降级 - deleteUserFallback !!!");
        System.err.println("请求的用户ID: " + userId + ", 异常信息: " + throwable.getMessage());
        return "操作过于频繁，请稍后再试 (限流保护)";
    }

}