package ynu.edu.controller;


import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.annotation.Resource;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ynu.edu.entity.Cart;
import ynu.edu.entity.CommonResult;
import ynu.edu.entity.User;
import ynu.edu.feign.ServiceProviderService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Resource
    private ServiceProviderService serviceInstance;

    @CircuitBreaker(name="backendA",fallbackMethod = "getCartByIdDown")
    @GetMapping("/getCartById/{userId}")
    @LoadBalanced
    @TimeLimiter(name = "timelimiterA",fallbackMethod = "getCartByIdDown")
    public CompletableFuture<User> getCartById(@PathVariable("userId") Integer userId){


        CompletableFuture<User> user = CompletableFuture.supplyAsync(() ->{return serviceInstance.GetUserById(userId);});

        System.out.println("正常执行!");

        return user;
    }
    public CompletableFuture<User> getCartByIdDown(Integer userId,Exception e){
        String message="该服务器火爆，请稍后再试！";
        System.out.println(message);
        CompletableFuture<User> result = CompletableFuture.supplyAsync(()->{
            return new CommonResult<>(440,"fallback",new User()).getResult();
        });
        return result;
    }

}