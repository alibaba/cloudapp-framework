package com.alibaba.cloudapp.hsf.demo;

import com.alibaba.cloudapp.hsf.demo.service.HelloService;
import com.alibaba.cloudapp.hsf.demo.service.impl.HelloServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderConfiguration {
    
    @Bean
    public HelloService helloService() {
        return new HelloServiceImpl();
    }
}
