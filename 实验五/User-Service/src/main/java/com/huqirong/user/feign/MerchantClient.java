package com.huqirong.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "merchant-service", fallbackFactory = MerchantClientFallbackFactory.class)
public interface MerchantClient {
    
    /**
     * 根据ID查询商家
     */
    @GetMapping("/merchants/{id}")
    Object getMerchantById(@PathVariable("id") Integer id);
} 