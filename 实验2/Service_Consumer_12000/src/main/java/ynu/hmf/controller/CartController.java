package ynu.hmf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ynu.hmf.entity.User;
import ynu.hmf.feign.UserFeignClient;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private UserFeignClient userFeignClient;

    @GetMapping("/getUser/{userId}")
    public User getUser(@PathVariable("userId") Integer userId) {
        return userFeignClient.getUserById(userId);
    }

    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        return userFeignClient.createUser(user);
    }

    @PutMapping("/updateUser/{userId}")
    public User updateUser(@PathVariable("userId") Integer userId, @RequestBody User user) {
        return userFeignClient.updateUser(userId, user);
    }

    @DeleteMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable("userId") Integer userId) {
        return userFeignClient.deleteUser(userId);
    }
}