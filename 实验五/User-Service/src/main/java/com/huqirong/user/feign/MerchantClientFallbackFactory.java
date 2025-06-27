package com.huqirong.user.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MerchantClientFallbackFactory implements FallbackFactory<MerchantClient> {
    
    @Override
    public MerchantClient create(Throwable throwable) {
        log.error("商家服务调用失败", throwable);
        return new MerchantClient() {
            @Override
            public Object getMerchantById(Integer id) {
                log.warn("商家服务降级：getMerchantById, id={}", id);
                return null;
            }
        };
    }
} 