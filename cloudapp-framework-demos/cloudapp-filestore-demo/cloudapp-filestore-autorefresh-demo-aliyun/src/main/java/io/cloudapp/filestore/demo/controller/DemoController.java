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

package io.cloudapp.filestore.demo.controller;

import io.cloudapp.api.filestore.BucketManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    
    private final static Logger logger =
            LoggerFactory.getLogger(DemoController.class);
    
    @Autowired
    BucketManager bucketManager;
    
    @RequestMapping("/getLocation")
    public String getLocation() {
        String bucketName = "testoss987";
        String location = bucketManager.getBucketLocation(bucketName);
        logger.info("bucket {}  location: {}", bucketName, location);
        return location;
    }
    
}