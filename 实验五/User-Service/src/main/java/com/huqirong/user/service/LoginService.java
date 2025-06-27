package com.huqirong.user.service;

import com.huqirong.user.pojo.User;

public interface LoginService {
    User login(String username, String password);
    void register(User user) throws Exception;
    User findByUsername(String username);
} 