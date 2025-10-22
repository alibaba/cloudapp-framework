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

package com.alibaba.cloudapp.config.manager.demo.controller;

import com.aliyuncs.acm.model.v20200206.DeployConfigurationRequest;
import com.alibaba.cloudapp.api.config.ConfigManager;
import com.alibaba.cloudapp.api.config.ConfigObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemoConfigManagerController {

    public static final Logger logger = LoggerFactory.getLogger(
            DemoConfigManagerController.class);

    @Autowired
    private ConfigManager configManager;

    /**
     * curl -XPOST 'http://127.0.0.1:8080/config?name=/DEFAULT_GROUP/test0911&content=abcd1'
     * curl -XPOST 'http://127.0.0.1:8080/config?name=/test0911&content=abcd1'
     *
     * @param name
     * @param content
     * @return
     */
    @ResponseBody
    @PostMapping("/config")
    public boolean publishConfig(
            @RequestParam("name") String name,
            @RequestParam("content") String content) {
        boolean result = configManager.publish(name, content);
        logger.info("publishConfig name={},content={},result={}", name, content,
                result
        );
        return result;
    }

    /**
     * curl -XPOST 'http://127.0.0.1:8080/configAdvance?name=test0913&content=abcd3=a&type=properties&tags=cloudapp&desc=this is a desc'
     *
     * @param name
     * @param content
     * @param type
     * @param tags
     * @param desc
     * @return
     */
    @ResponseBody
    @PostMapping("/configAdvance")
    public boolean publishConfigAdvance(
            @RequestParam("name") String name,
            @RequestParam("content") String content,
            @RequestParam("type") String type,
            @RequestParam("tags") String tags,
            @RequestParam("desc") String desc) {
        DeployConfigurationRequest deployConfigurationRequest = new DeployConfigurationRequest();
        deployConfigurationRequest.setType(type);
        deployConfigurationRequest.setTags(tags);
        deployConfigurationRequest.setDesc(desc);
        deployConfigurationRequest.setContent(content);
        ConfigObject<Object> configObject =
                ConfigObject.builder().configName(name).content(
                        deployConfigurationRequest).build();
        boolean result = configManager.publishConfig(configObject);
        logger.info("publishConfigByConfigObject name={},content={},result={}",
                name, content, result
        );
        return result;
    }

    /**
     * curl -XDELETE 'http://127.0.0.1:8080/config?name=/DEFAULT_GROUP/test0911&content=abcd1'
     *
     * @param name
     * @return
     */
    @ResponseBody
    @DeleteMapping("/config")
    public boolean deleteConfig(@RequestParam("name") String name) {
        boolean result = configManager.deleteConfig(name);
        logger.info("deleteConfig name={},result={}", name, result);
        return result;
    }

}
