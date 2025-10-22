package com.alibaba.cloudapp.hsf.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @RequestMapping("/health")
    public String healthCheck() {
        System.out.println("Consumer health check");
        return "success";
    }

}
