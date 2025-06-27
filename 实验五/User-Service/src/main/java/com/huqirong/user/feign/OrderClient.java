package com.huqirong.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "order-service", fallbackFactory = OrderClientFallbackFactory.class)
public interface OrderClient {
    
    /**
     * 根据用户ID查询订单列表
     */
    @GetMapping("/orders/user/{userId}")
    Object getOrdersByUserId(@PathVariable("userId") Long userId);
} 