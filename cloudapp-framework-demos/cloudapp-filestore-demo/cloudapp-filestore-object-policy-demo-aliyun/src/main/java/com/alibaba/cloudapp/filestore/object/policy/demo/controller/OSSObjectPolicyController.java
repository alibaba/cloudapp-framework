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

package com.alibaba.cloudapp.filestore.object.policy.demo.controller;

import com.alibaba.cloudapp.api.filestore.ObjectPolicyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OSSObjectPolicyController {

    private final static Logger logger = LoggerFactory.getLogger(OSSObjectPolicyController.class);

    @Autowired
    ObjectPolicyManager objectPolicyManager;

    @RequestMapping("/getPolicy")
    public String getPolicy(@RequestParam String bucketName, @RequestParam String objectName) {
        String objectPolicy = objectPolicyManager.getObjectPolicy(
                bucketName, objectName);
        logger.info("object {} policy: {}", objectName, objectPolicy);
        return objectPolicy;
    }

    @RequestMapping("/grantAccessPermissions")
    public void grantAccessPermissions(@RequestParam String bucketName, @RequestParam String objectName, @RequestParam String accessAcl) {
        objectPolicyManager.grantAccessPermissions(bucketName, objectName, accessAcl);
    }

    @RequestMapping("/deleteObjectPolicy")
    public boolean deleteObjectPolicy(@RequestParam String bucketName, @RequestParam String objectName) {
        boolean result = objectPolicyManager.deleteObjectPolicy(bucketName, objectName);
        logger.info("delete object {} policy result is: {}", objectName,
                result
        );
        return result;
    }

}

