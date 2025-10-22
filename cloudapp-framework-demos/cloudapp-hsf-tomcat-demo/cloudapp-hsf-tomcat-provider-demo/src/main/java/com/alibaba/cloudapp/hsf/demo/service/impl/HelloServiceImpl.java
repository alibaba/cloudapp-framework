package com.alibaba.cloudapp.hsf.demo.service.impl;

import com.alibaba.boot.hsf.annotation.HSFProvider;
import com.alibaba.cloudapp.hsf.demo.service.HelloService;

@HSFProvider(serviceInterface = HelloService.class, serviceVersion = "1.0.0")
public class HelloServiceImpl implements HelloService {
    @Override
    public String echo(String string) {
        return string;
    }
}
