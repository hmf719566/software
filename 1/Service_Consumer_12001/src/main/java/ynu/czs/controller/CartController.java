package ynu.czs.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ynu.czs.entity.User;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Resource
    private RestTemplate restTemplate;

    //  GET 方法
    @GetMapping("/getUser/{userId}")
    public User getUser(@PathVariable Integer userId) {
        return restTemplate.getForObject(
                "http://provider-service/user/getUserById/{userId}",
                User.class,
                userId
        );
    }

    // POST 方法
    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        return restTemplate.postForObject(
                "http://provider-service/user/createUser",
                user,
                User.class
        );
    }

    //  PUT 方法
    @PutMapping("/updateUser/{userId}")
    public String updateUser(@PathVariable Integer userId, @RequestBody User updatedUser) {
        try {
            restTemplate.put(
                    "http://provider-service/user/updateUser/{userId}",
                    updatedUser,
                    userId
            );
            return "用户 " + userId + " 的更新请求已成功发送。";
        } catch (Exception e) {

            return "更新用户 " + userId + " 失败：" + e.getMessage();
        }
    }

    //  DELETE 方法
    @DeleteMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        try {
            restTemplate.delete(
                    "http://provider-service/user/updateUser/{userId}",
                    userId
            );
            return "用户 " + userId + " 已成功删除。";
        } catch (Exception e) {
            return "删除用户 " + userId + " 失败：" + e.getMessage();
        }
    }
}

