package com.huqirong.user.mapper;

import com.huqirong.user.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User findByUsername(String username);
    // 根据名字查询用户列表
    List<User> selectUsersByName(String username);
    // 根据ID查询用户
    User findById(Long id);
    // 插入新用户
    int createUser(User user);
    // 删除用户根据用户名
    void deleteUserByUsername(String username);
    // 更新用户
    void updateUser(Long id, User updatedUser);
    // 根据用户 ID 删除用户
    void deleteUserById(Long id);
} 