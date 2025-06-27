package com.huqirong.user.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderClientFallbackFactory implements FallbackFactory<OrderClient> {
    
    @Override
    public OrderClient create(Throwable throwable) {
        log.error("订单服务调用失败", throwable);
        return new OrderClient() {
            @Override
            public Object getOrdersByUserId(Long userId) {
                log.warn("订单服务降级：getOrdersByUserId, userId={}", userId);
                return null;
            }
        };
    }
} 