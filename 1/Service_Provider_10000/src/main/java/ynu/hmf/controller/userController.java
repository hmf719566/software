package ynu.hmf.controller;

import org.springframework.web.bind.annotation.*;
import ynu.hmf.entity.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class userController {

    private static List<User> users = new ArrayList<>();
    static {
        users.add(new User(1, "zhangsan from 10000", "123456"));
    }

    @GetMapping("/getUserById/{userId}")
    public User GetUserById(@PathVariable("userId") Integer userId) {
        return users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    // POST（新增用户）
    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        users.add(user);
        return user;
    }

    // PUT（更新用户）
    @PutMapping("/updateUser/{userId}")
    public User updateUser(
            @PathVariable("userId") Integer userId,
            @RequestBody User user
    ) {
        User existing = users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        existing.setUserName(user.getUserName());
        existing.setPassword(user.getPassword());
        System.out.println("Updating user with ID: " + userId);
        System.out.println("Available users: " + users);
        return existing;
    }

    // DELETE（删除用户）
    @DeleteMapping("/deleteUser/{userId}")
    public String deleteUser(
            @PathVariable("userId") Integer userId
    ) {
        boolean exists = users.stream().anyMatch(u -> u.getUserId().equals(userId));
        if (!exists) {
            throw new RuntimeException("用户不存在");
        }
        users.removeIf(u -> u.getUserId().equals(userId));
        return "删除成功（ID：" + userId + "）";
    }

}
