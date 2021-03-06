package cn.com.koriesh.memo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
//@EnableEurekaClient
//@EnableFeignClients
@SpringBootApplication
public class MemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemoApplication.class, args);
    }
}


