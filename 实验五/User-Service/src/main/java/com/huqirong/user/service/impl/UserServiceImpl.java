package com.huqirong.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huqirong.user.mapper.UserMapper;
import com.huqirong.user.pojo.PageBean;
import com.huqirong.user.pojo.User;
import com.huqirong.user.service.UserService;
import com.huqirong.user.utils.JwtUtils;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private static final String USER_SERVICE = "userService";

    @Override
    @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "getUsersFallback")
    @RateLimiter(name = USER_SERVICE)
    @Retry(name = USER_SERVICE)
    public PageBean getUsers(int pageNum, int pageSize, String username) {
        log.info("查询用户列表，页码：{}，每页数量：{}，用户名：{}", pageNum, pageSize, username);
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.selectUsersByName(username);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return new PageBean(pageInfo.getTotal(), pageInfo.getList());
    }

    // 服务降级方法
    public PageBean getUsersFallback(int pageNum, int pageSize, String username, Exception ex) {
        log.error("查询用户列表失败，触发服务降级：{}", ex.getMessage());
        return new PageBean(0L, new ArrayList<>());
    }

    @Override
    @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "createUserFallback")
    @Retry(name = USER_SERVICE)
    public void createUser(User user) {
        log.info("创建用户：{}", user.getUsername());
        // 插入用户数据，MyBatis 会自动返回生成的主键并填充到 User 对象的 id 字段
        int rows = userMapper.createUser(user);
        // 检查插入操作是否成功
        if (rows > 0) {
            log.info("用户创建成功，ID: {}", user.getId());
        } else {
            throw new RuntimeException("用户创建失败");
        }
    }

    // 服务降级方法
    public void createUserFallback(User user, Exception ex) {
        log.error("创建用户失败，触发服务降级：{}", ex.getMessage());
        throw new RuntimeException("服务暂时不可用，请稍后再试");
    }

    @Override
    @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "deleteUserByUsernameFallback")
    @Retry(name = USER_SERVICE)
    public boolean deleteUserByUsername(String username) {
        log.info("删除用户：{}", username);
        // 先根据用户名找到用户 ID
        User user = userMapper.findByUsername(username);
        if (user == null) {
            log.warn("用户不存在：{}", username);
            return false; // 用户不存在
        }
        // 删除用户
        userMapper.deleteUserByUsername(username);
        log.info("用户删除成功：{}", username);
        return true;
    }

    // 服务降级方法
    public boolean deleteUserByUsernameFallback(String username, Exception ex) {
        log.error("删除用户失败，触发服务降级：{}", ex.getMessage());
        return false;
    }

    @Override
    @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "updateUserFallback")
    @Retry(name = USER_SERVICE)
    public boolean updateUser(Long id, User updatedUser) {
        log.info("更新用户，ID：{}", id);
        User existingUser = userMapper.findById(id);
        if (updatedUser == null) {
            log.warn("数据格式错误或未填写完全");
            return false;
        }
        if (existingUser != null) {
            userMapper.updateUser(id, updatedUser);
            log.info("用户更新成功，ID：{}", id);
            return true;
        }
        log.warn("用户不存在，ID：{}", id);
        return false;
    }

    // 服务降级方法
    public boolean updateUserFallback(Long id, User updatedUser, Exception ex) {
        log.error("更新用户失败，触发服务降级：{}", ex.getMessage());
        return false;
    }

    @Override
    @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "isAdminFallback")
    public boolean isAdmin(String token) {
        // 不再检查角色，直接返回true
        log.info("权限检查已禁用，允许所有用户执行管理员操作");
        return true;
        
        /* 原代码注释掉
        Claims claims = JwtUtils.parseJWT(token.replace("Bearer ", ""));
        String role = (String) claims.get("role");
        if("admin".equals(role)){
            log.info("管理员执行操作");
            return true;
        }else{
            log.info("用户权限不足，仅管理员能执行该操作");
            return false;
        }
        */
    }

    // 服务降级方法
    public boolean isAdminFallback(String token, Exception ex) {
        log.error("权限检查失败，触发服务降级：{}", ex.getMessage());
        return false; // 降级时不授予管理员权限
    }
} 