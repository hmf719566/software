package com.huqirong.user.service.impl;

import com.huqirong.user.mapper.LoginMapper;
import com.huqirong.user.pojo.User;
import com.huqirong.user.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public User login(String username, String password) {
        // 根据用户名和密码查询用户
        log.info("用户 {} 尝试登录", username);
        return loginMapper.findByUsernameAndPassword(username, password);
    }

    @Override
    @Transactional
    public void register(User user) throws Exception {
        // 检查用户名是否已存在
        User existingUser = loginMapper.findByUsername(user.getUsername());
        if (existingUser != null) {
            log.error("注册失败，用户名 {} 已存在", user.getUsername());
            throw new Exception("用户名已存在");
        }
        
        // 设置默认角色为USER（如果未设置）
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        
        // 创建新用户
        log.info("注册新用户: {}", user.getUsername());
        loginMapper.createUser(user);
    }

    @Override
    public User findByUsername(String username) {
        return loginMapper.findByUsername(username);
    }
} 