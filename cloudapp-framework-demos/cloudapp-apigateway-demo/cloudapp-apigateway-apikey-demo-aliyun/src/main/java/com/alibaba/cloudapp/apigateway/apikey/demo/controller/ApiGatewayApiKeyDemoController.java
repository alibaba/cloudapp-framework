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

package com.alibaba.cloudapp.apigateway.apikey.demo.controller;

import com.alibaba.cloudapp.api.gateway.GatewayService;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class ApiGatewayApiKeyDemoController {

    private static final Logger logger = LoggerFactory.getLogger(ApiGatewayApiKeyDemoController.class);

    @Autowired
    GatewayService gatewayService;

    @Value("${io.cloudapp.apigateway.aliyun.brokerUrl:}")
    private String brokerUrl;

    @RequestMapping("/get")
    public ResponseEntity<String> get(@RequestParam(defaultValue = "/") String path) {
        String result = gatewayService.get(brokerUrl + path, String.class);
        logger.info("The simple get request response is: " + "\n" + result);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/asyncGet")
    public Future<String> asyncGet(@RequestParam(defaultValue = "/") String path) {
        Future<String> future = gatewayService.asyncGet(brokerUrl + path, String.class);
        if (future != null) {
            while (!future.isDone()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                logger.info("The simple async get request response is: " + "\n" + future.get());
                return future;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return future;
    }

}