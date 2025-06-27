package com.huqirong.user.service;

import com.huqirong.user.pojo.PageBean;
import com.huqirong.user.pojo.User;

public interface UserService {
    // 查询所有用户
    PageBean getUsers(int pageNum, int pageSize, String username);
    // 新增用户
    void createUser(User user);
    // 删除用户
    boolean deleteUserByUsername(String username);
    // 更新用户
    boolean updateUser(Long id, User updatedUser);
    // 检查是否为管理员
    boolean isAdmin(String token);
} 