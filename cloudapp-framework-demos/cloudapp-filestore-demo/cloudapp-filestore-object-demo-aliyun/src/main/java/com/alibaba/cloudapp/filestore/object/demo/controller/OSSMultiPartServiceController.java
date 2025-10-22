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

package com.alibaba.cloudapp.filestore.object.demo.controller;

import com.alibaba.cloudapp.api.filestore.MultiPartsService;
import com.alibaba.cloudapp.model.Pairs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class OSSMultiPartServiceController {

    private final static Logger logger = LoggerFactory.getLogger(OSSMultiPartServiceController.class);

    @Autowired
    MultiPartsService multiPartsService;

    private String defaultBucketName = "test0610";

//    request example: GET http://localhost:8080/uploadObjects?bucketName=test0610&objectName=testab2.txt&filePaths=D:\testa.txt, D:\testb.txt
    @RequestMapping("/uploadObjects")
    public void uploadObjects(@RequestParam String bucketName, @RequestParam String objectName, @RequestParam String filePaths) throws IOException, InterruptedException {

        String[] list =  Arrays.stream(filePaths.split(","))
                .map(String::trim)
                .toArray(String[]::new);

        List<Pairs.Pair<String, InputStream>> parts = new ArrayList<>();

        for (String fileName : list) {
            InputStream inputStream = new FileInputStream(fileName);
            parts.add(new Pairs.Pair<>(fileName, inputStream));
        }

        Map<String, Integer> result = multiPartsService.uploadObjects(
                bucketName, objectName, parts);

        while (parts.size() != result.size()) {
            logger.info("Object {} is uploading...", objectName);
            Thread.sleep(1000);
        }

        logger.info("Upload objects result is: {}", result);

    }

    @RequestMapping("/uploadBigFile")
    public boolean uploadBigFile(@RequestParam String bucketName, @RequestParam String objectName, @RequestParam String filePath, @RequestParam(defaultValue = "5242880") int partSize) throws InterruptedException {
        Path path = Paths.get(filePath);

        boolean result = multiPartsService.uploadBigFile(bucketName,
                objectName, path, partSize
        );

        Thread.sleep(5000);

        logger.info("Upload big file result is: {}", result);

        return result;
    }

}