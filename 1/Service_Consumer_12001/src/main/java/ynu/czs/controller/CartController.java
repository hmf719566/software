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

    // 调用 GET 方法
    @GetMapping("/getUser/{userId}")
    public User getUser(@PathVariable Integer userId) {
        return restTemplate.getForObject(
                "http://provider-service/user/getUserById/{userId}",
                User.class,
                userId
        );
    }

    // 调用 POST 方法
    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        return restTemplate.postForObject(
                "http://provider-service/user/createUser",
                user,
                User.class
        );
    }

    // 调用 PUT 方法
    @PutMapping("/updateUser/{userId}")
    public User updateUser(@PathVariable Integer userId, @RequestBody User user) {
        restTemplate.put(
                "http://provider-service/user/updateUser/{userId}",
                user,
                userId
        );
        return user;
    }

    // 调用 DELETE 方法
    @DeleteMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        restTemplate.delete(
                "http://provider-service/user/deleteUser/{userId}",
                userId
        );
        return "已触发删除操作";
    }
}
