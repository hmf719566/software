package com.huqirong.user.mapper;

import com.huqirong.user.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;

@Mapper
public interface LoginMapper {

    // 根据用户名查询用户
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    // 根据用户名和密码查询用户
    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    User findByUsernameAndPassword(String username, String password);

    // 插入新用户
    @Insert("INSERT INTO users (username, password, role) VALUES (#{username}, #{password}, #{role})")
    void createUser(User user);
} 