package com.huqirong.user.controller;

import com.huqirong.user.anno.Log;
import com.huqirong.user.pojo.Result;
import com.huqirong.user.pojo.User;
import com.huqirong.user.service.LoginService;
import com.huqirong.user.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Log("用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody User loginUser) {
        // 检查手机号是否已注册
        User existingUser = loginService.findByUsername(loginUser.getUsername());
        if (existingUser == null) {
            return Result.error("手机号未注册");
        }

        // 验证密码
        User user = loginService.login(loginUser.getUsername(), loginUser.getPassword());
        if (user != null) {
            // 将用户ID和角色添加到 JWT 的 Claims 中
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("role", user.getRole());
            claims.put("username", user.getUsername());
            // 生成 JWT token
            String token = JwtUtils.generateJwt(claims);  // 传递用户ID和角色信息
            // 封装返回的数据
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            data.put("token", token);
            log.info("用户 {} 登录成功, token: {}", user.getUsername(), token);
            return Result.success(data);  // 返回成功的 Result，包含用户信息、权限和 token
        } else {
            log.warn("用户 {} 登录失败，密码错误", loginUser.getUsername());
            return Result.error("登录失败，用户名或密码错误");
        }
    }

    @Log("用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        try {
            loginService.register(user);
            log.info("用户 {} 注册成功", user.getUsername());
            return Result.success("注册成功");
        } catch (Exception e) {
            log.error("用户 {} 注册失败: {}", user.getUsername(), e.getMessage());
            return Result.error("注册失败：" + e.getMessage());
        }
    }
} 