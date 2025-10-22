package com.alibaba.cloudapp.microservice.aliyun.demo;

import java.util.List;
import java.util.Map;

/**
 * Demo service interface
 */
public interface DemoService {
    
    /**
     * Tag method for traffic label testing
     */
    String tag(String name);
    
    /**
     * Say hello with name
     */
    String sayHello(String name);
    
    /**
     * Get user info by id
     */
    Map<String, Object> getUserInfo(Long userId);
    
    /**
     * Get user list by ids
     */
    List<Map<String, Object>> getUserList(List<Long> userIds);
    
    /**
     * Echo test method
     */
    String echo(String message);
}
