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

package com.alibaba.cloudapp.messaging.rocketmq.demo.controller;

import com.alibaba.cloudapp.messaging.rocketmq.CloudAppRocketProducer;
import com.alibaba.cloudapp.messaging.rocketmq.demo.service.RocketDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RocketProducerDemoController {

//    static ThreadLocal<Producer> threadLocal = new ThreadLocal<>();

    private static final Logger logger = LoggerFactory.getLogger(RocketProducerDemoController.class);

    @Autowired
    @Qualifier("rocketProducer")
    CloudAppRocketProducer rocketProducer;
    @Autowired
    RocketDemoService service;

    @RequestMapping("/testRocketProducer")
    public void testRocketProducer() {
        service.sendData();
    }
    
    @RequestMapping("/testAsync")
    public Object[] testAsync() {
       return service.sendAsync();
    }


}
