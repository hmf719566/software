package com.huqirong.user.controller;

import com.huqirong.user.pojo.Result;
import com.huqirong.user.pojo.User;
import com.huqirong.user.service.UserService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RefreshScope
@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // 查询所有用户
    @GetMapping
    @RateLimiter(name = "userController")
    @Bulkhead(name = "userController")
    public Result getAllUsers(@RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "5") int pageSize,
                              @RequestParam(required = false) String username) {
        return Result.success(userService.getUsers(pageNum, pageSize, username));
    }
    
    // 新增用户
    @PostMapping
    @RateLimiter(name = "userController")
    @Bulkhead(name = "userController")
    public Result createUser(@RequestBody User user, @RequestHeader("Authorization") String token) {
        if(!userService.isAdmin(token)){
            return Result.error("没有权限");
        }
        userService.createUser(user);
        return Result.success(user);
    }
    
    // 删除用户
    @DeleteMapping("/{username}")
    @RateLimiter(name = "userController")
    @Bulkhead(name = "userController")
    public Result deleteUser(@PathVariable String username, @RequestHeader("Authorization") String token) {
        if(!userService.isAdmin(token)){
            return Result.error("没有权限");
        }
        boolean deleted = userService.deleteUserByUsername(username);
        if (deleted) {
            return Result.success("用户删除成功");
        } else {
            return Result.error("删除失败，用户不存在");
        }
    }

    // 更新用户信息
    @PutMapping("/{id}")
    @RateLimiter(name = "userController")
    @Bulkhead(name = "userController")
    public Result updateUser(@PathVariable Long id, @RequestBody User updatedUser, @RequestHeader("Authorization") String token) {
        if(!userService.isAdmin(token)){
            return Result.error("没有权限");
        }
        boolean updated = userService.updateUser(id, updatedUser);
        if (updated) {
            return Result.success(updatedUser);
        } else {
            return Result.error("更新失败，用户不存在");
        }
    }
    
    // 健康检查端点
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "user-service");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    // 配置中心内容验证接口
    @Value("${test.message:未获取到配置}")
    private String testMessage;

    @GetMapping("/config/test")
    public String testConfig() {
        return "配置内容: " + testMessage;
    }
} 