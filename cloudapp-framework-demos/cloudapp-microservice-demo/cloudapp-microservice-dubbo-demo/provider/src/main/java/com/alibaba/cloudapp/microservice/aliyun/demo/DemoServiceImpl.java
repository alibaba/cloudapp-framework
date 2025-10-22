/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.alibaba.cloudapp.microservice.aliyun.demo;

import com.alibaba.cloudapp.api.microservice.TrafficService;
import io.opentelemetry.context.Scope;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service(version = "1.0.0", group = "DUBBO")
public class DemoServiceImpl implements DemoService {

    private static final Logger logger = LoggerFactory.getLogger(DubboProviderDemoApplication.class);

    @Autowired
    private TrafficService trafficService;

    @Override
    public String tag(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        logger.info("traffic label before inject: {}", trafficService.getCurrentTrafficLabel());
        trafficService.withTrafficLabel(name);
        logger.info("traffic label after inject: {}", trafficService.getCurrentTrafficLabel());
        return name;
    }

    @Override
    public String sayHello(String name) {
        if (name == null || name.isEmpty()) {
            return "Hello, World!";
        }
        return "Hello, " + name + "!";
    }

    @Override
    public Map<String, Object> getUserInfo(Long userId) {
        if (userId == null) {
            return Collections.emptyMap();
        }
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", userId);
        userInfo.put("name", "User" + userId);
        userInfo.put("email", "user" + userId + "@example.com");
        userInfo.put("createTime", System.currentTimeMillis());
        
        logger.info("Get user info for userId: {}", userId);
        return userInfo;
    }

    @Override
    public List<Map<String, Object>> getUserList(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Long userId : userIds) {
            result.add(getUserInfo(userId));
        }
        
        logger.info("Get user list for userIds: {}", userIds);
        return result;
    }

    @Override
    public String echo(String message) {
        logger.info("Echo message: {}", message);
        return "Echo: " + (message != null ? message : "null");
    }
}
