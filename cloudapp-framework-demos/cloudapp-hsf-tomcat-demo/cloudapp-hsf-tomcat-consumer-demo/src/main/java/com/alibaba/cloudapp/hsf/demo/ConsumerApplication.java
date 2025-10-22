package com.alibaba.cloudapp.hsf.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ConsumerApplication extends SpringBootServletInitializer {
    
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ConsumerApplication.class);
    }
    
}
