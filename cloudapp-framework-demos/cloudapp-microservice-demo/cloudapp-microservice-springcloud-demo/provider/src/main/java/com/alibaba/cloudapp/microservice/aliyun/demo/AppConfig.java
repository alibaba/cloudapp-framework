package com.alibaba.cloudapp.microservice.aliyun.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Service
@RefreshScope
public class AppConfig {

    @Value("${application.app.timeoutInMillis:1000}")
    private int timeoutInMillis = 1000;

    @Value("${application.app.label:unknown}")
    private String label = "unknown";


    public int getTimeoutInMillis() {
        return timeoutInMillis;
    }

    public void setTimeoutInMillis(int timeoutInMillis) {
        this.timeoutInMillis = timeoutInMillis;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
