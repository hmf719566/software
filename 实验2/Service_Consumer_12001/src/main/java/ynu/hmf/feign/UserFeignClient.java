package ynu.hmf.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ynu.hmf.entity.User;

@FeignClient(name = "provider-service")
public interface UserFeignClient {

    @GetMapping("/user/getUserById/{userId}")
    User getUserById(@PathVariable("userId") Integer userId);

    @PostMapping("/user/createUser")
    User createUser(@RequestBody User user);

    @PutMapping("/user/updateUser/{userId}")
    User updateUser(@PathVariable("userId") Integer userId, @RequestBody User user);

    @DeleteMapping("/user/deleteUser/{userId}")
    String deleteUser(@PathVariable("userId") Integer userId);
} 