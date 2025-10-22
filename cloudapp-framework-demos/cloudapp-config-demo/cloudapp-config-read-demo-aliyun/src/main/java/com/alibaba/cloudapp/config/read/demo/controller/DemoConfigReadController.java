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

package com.alibaba.cloudapp.config.read.demo.controller;

import com.alibaba.cloudapp.api.config.ConfigReadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

@RestController
public class DemoConfigReadController {

    public static final Logger logger = LoggerFactory.getLogger(
            DemoConfigReadController.class);

    @Autowired
    private ConfigReadService configReadService;

    /**
     * curl 'http://127.0.0.1:8080/config?name=test'
     * curl 'http://127.0.0.1:8080/config?name=/DEFAULT_GROUP/test'
     *
     * @param name
     * @return
     */
    @ResponseBody
    @GetMapping("/config")
    public String getConfig(@RequestParam("name") String name) {
        String result = configReadService.getConfig(name);
        logger.info("getConfig name={},result={}", name, result);
        return result;
    }

    /**
     * curl 'http://127.0.0.1:8080/listen?name=test'
     * curl 'http://127.0.0.1:8080/listen?name=/DEFAULT_GROUP/test'
     *
     * @param name
     * @return
     */
    @ResponseBody
    @GetMapping("/listen")
    public String listen(@RequestParam("name") String name) {
        String result = configReadService.getAndListen(name,
                new Consumer<String>() {

                    @Override
                    public void accept(
                            String o) {
                        logger.info(
                                "receive content {} for configName={}",
                                o, name
                        );
                    }
                }
        );
        logger.info("listenConfig name={},result={}", name, result);
        return result;
    }

}
