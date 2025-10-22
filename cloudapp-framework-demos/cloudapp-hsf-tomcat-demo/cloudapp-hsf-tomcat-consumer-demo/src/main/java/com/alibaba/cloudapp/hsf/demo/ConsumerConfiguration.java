package com.alibaba.cloudapp.hsf.demo;

import com.alibaba.boot.hsf.annotation.HSFConsumer;
import com.alibaba.cloudapp.hsf.demo.service.HelloService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfiguration {
    
    @HSFConsumer(clientTimeout = 3000, serviceVersion = "1.0.0")
    HelloService helloService;
}
