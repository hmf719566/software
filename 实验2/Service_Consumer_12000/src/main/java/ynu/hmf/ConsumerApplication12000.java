package ynu.hmf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import ynu.hmf.config.LoadBalancerConfig;


@SpringBootApplication
@EnableFeignClients
@LoadBalancerClient(name = "provider-service", configuration = LoadBalancerConfig.class)
public class ConsumerApplication12000 {
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args)  {
        SpringApplication.run(ConsumerApplication12000.class, args);
    }
}
