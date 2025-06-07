package ynu.hmf.config;

import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;


public class LoadBalancerConfig {
    
    @Bean
    public ReactorLoadBalancer<?> roundRobinLoadBalancer(Environment environment,
                                                         LoadBalancerClientFactory loadBalancerClientFactory) {
        String serviceName = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        // 使用轮询策略（默认策略）
        return new RoundRobinLoadBalancer(
                loadBalancerClientFactory.getLazyProvider(serviceName, ServiceInstanceListSupplier.class),
                serviceName
        );
    }
    
    // 如果想使用随机策略，可以取消下面的注释并注释掉上面的方法
    /*
    @Bean
    public ReactorLoadBalancer<?> randomLoadBalancer(Environment environment,
                                                    LoadBalancerClientFactory loadBalancerClientFactory) {
        String serviceName = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new RandomLoadBalancer(
                loadBalancerClientFactory.getLazyProvider(serviceName, ServiceInstanceListSupplier.class),
                serviceName
        );
    }
    */
} 