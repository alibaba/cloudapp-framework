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

package com.alibaba.cloudapp.apigateway.manager.demo.controller;

import com.alibaba.cloudapp.apigateway.aliyun.properties.ApiKeyProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.BasicProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.JwtProperties;
import com.alibaba.cloudapp.apigateway.aliyun.service.ApiGatewayManager;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/manager")
public class ApiGatewayManagerDemoController {

    private static final Logger logger = LoggerFactory.getLogger(
            ApiGatewayManagerDemoController.class);

    @Autowired(required = false)
    ApiGatewayManager apiGatewayManager;

    @Value("${io.cloudapp.apigateway.aliyun.server.instanceId}")
    private String gwInstanceId;

    @RequestMapping("/checkConsumerExists")
    public boolean checkConsumerExists(@RequestParam String consumerName) {
        boolean exists = false;
        try {
            exists = apiGatewayManager.checkConsumerExists(consumerName, gwInstanceId);
        } catch (CloudAppException e) {
            logger.error("Check consumer exists failed.", e);
        }
        logger.info("The consumer exists is: " + exists + ", in CSB.");
        return exists;
    }

    @RequestMapping("/createApiKeyConsumer")
    public void createApiKeyConsumer(@RequestParam String consumerName, @RequestParam(defaultValue = "") String consumerGroup, @RequestParam String apiKey) {
        try {
            ApiKeyProperties properties = new ApiKeyProperties();
            properties.setApiKey(apiKey);

            List<String> groups = Arrays.stream(consumerGroup.split(","))
                    .map(String::trim).collect(Collectors.toList());

            apiGatewayManager.createApiKeyConsumer(consumerName, gwInstanceId, groups, properties);
            logger.info("The consumer is created in CSB.");
        } catch (CloudAppException e) {
            logger.error("Create consumer failed.", e);
        }
    }

    @RequestMapping("/createBasicConsumer")
    public void createBasicConsumer(@RequestParam String consumerName, @RequestParam(defaultValue = "") String consumerGroup, @RequestBody BasicProperties basic) {
        List<String> groups = Arrays.stream(consumerGroup.split(","))
                .map(String::trim).collect(Collectors.toList());

        apiGatewayManager.createBasicConsumer(consumerName, gwInstanceId, groups, basic);
    }

    @RequestMapping("/createJwtConsumer")
    public void createJwtConsumer(@RequestParam String consumerName, @RequestParam(defaultValue = "") String consumerGroup, @RequestBody JwtProperties jwt) {
        List<String> groups = Arrays.stream(consumerGroup.split(","))
                .map(String::trim).collect(Collectors.toList());

        apiGatewayManager.createJwtConsumer(consumerName, gwInstanceId, groups, jwt);
    }

}