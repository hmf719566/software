package ynu.hmf.config;

import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


public class LoadBalancerConfig {
    
//    @Bean
//    public ReactorLoadBalancer<?> roundRobinLoadBalancer(Environment environment,
//                                                         LoadBalancerClientFactory loadBalancerClientFactory) {
//        String serviceName = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
//        // 使用轮询策略（默认策略）
//        return new RoundRobinLoadBalancer(
//                loadBalancerClientFactory.getLazyProvider(serviceName, ServiceInstanceListSupplier.class),
//                serviceName
//        );
//    }
    
    // 使用随机策略

    @Bean
    public ReactorLoadBalancer<?> randomLoadBalancer(Environment environment,
                                                    LoadBalancerClientFactory loadBalancerClientFactory) {
        String serviceName = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new RandomLoadBalancer(
                loadBalancerClientFactory.getLazyProvider(serviceName, ServiceInstanceListSupplier.class),
                serviceName
        );
    }

}