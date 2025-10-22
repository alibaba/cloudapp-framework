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

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.TagSet;
import com.alibaba.cloudapp.api.common.Pagination;
import com.alibaba.cloudapp.api.filestore.BucketManager;
import com.alibaba.cloudapp.api.filestore.StorageObjectService;
import com.alibaba.cloudapp.api.filestore.model.ObjectItem;
import com.alibaba.cloudapp.api.filestore.model.ObjectMetadata;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
public class OSSObjectStorageServiceController {

    private final static Logger logger = LoggerFactory.getLogger(OSSObjectStorageServiceController.class);

    @Autowired
    StorageObjectService storageObjectService;

    private String defaultBucketName = "test0610";

    @RequestMapping("/putObject")
    public boolean putObject(@RequestParam String bucketName, @RequestParam String objectPath, @RequestParam String filePath,  @RequestParam String contentType) {
        boolean result = false;

        try {
//            result = storageObjectService.putObject(bucketName, objectPath, new ClassPathResource("test.txt").getInputStream(), "");
//            result = storageObjectService.putObject(bucketName, objectPath, new ClassPathResource("test.html").getInputStream(), "text/html");
//            result = storageObjectService.putObject(bucketName, objectPath,
//                    new ClassPathResource("test2.txt").getInputStream(), contentType);
            InputStream inputStream = new FileInputStream(filePath);
            result = storageObjectService.putObject(bucketName, objectPath, inputStream, contentType);
        } catch (IOException e) {
            logger.error("Error occurred when reading file", e);
        }
        logger.info("Put object result is: {}", result);
        return result;
    }

//    TODO Exception: The source file is not actually copied to the destination bucket.
    @RequestMapping("/copy")
    public boolean copy(@RequestParam String sourceBucketName, @RequestParam String sourcePath, @RequestParam String targetBucketName, @RequestParam String targetPath, @RequestParam(defaultValue = "false") boolean override) {
        boolean result = storageObjectService.copy(sourceBucketName, sourcePath, targetBucketName, targetPath, override);
        logger.info("Copy object result is: {}", result);
        return result;
    }

//    download file from OSS.
    @RequestMapping("/getObject")
    public void getObject(@RequestParam String bucketName, @RequestParam String objectPath) {
        InputStream object = storageObjectService.getObject(bucketName, objectPath);
        Path testResourcesDir = Paths.get(System.getProperty("user.dir"))
                .resolve("cloudapp-framework-demos")
                .resolve("cloudapp-filestore-demo")
                .resolve("cloudapp-filestore-object-demo-aliyun")
                .resolve("src")
                .resolve("main")
                .resolve("resources");
        try (OutputStream outputStream = Files.newOutputStream(
                Paths.get(testResourcesDir.resolve("test-download-file.txt").toString()))) {
            byte[] buffer = new byte[4096];
            int n;
            while ((n = object.read(buffer)) != -1) {
                outputStream.write(buffer, 0, n);
            }
        } catch (IOException e) {
            logger.error("Error occurred when writing file to disk", e);
        }
    }

    @RequestMapping("/listObjectVersions")
    public String listObjectVersions(@RequestParam String bucketName, @RequestParam String objectPath,  @RequestParam String sinceVersion, @RequestParam(defaultValue = "1") int count) throws CloudAppException {
        Collection<String> versions = storageObjectService.listObjectVersions(
                bucketName, objectPath,
                sinceVersion,
                count
        );
        logger.info("List object versions result is: {}", versions);
        return  JSONObject.toJSONString(versions);
    }

    @RequestMapping("/getObjectTag")
    public String  getObjectTag(@RequestParam String bucketName, @RequestParam String objectPath) {
        OSS oss = (OSS) storageObjectService.getDelegatingStorageClient();
        TagSet tagSet = oss.getObjectTagging(bucketName, objectPath);
        tagSet.getAllTags().forEach((key, value) -> logger.info(
                "Object {} tag key is: {}, value is: {}", objectPath, key, value));
        String result = JSONObject.toJSONString(tagSet);
        return result;
    }

//    TODO ERROR: RestoreObject operation does not support this object storage class. Currently, only standard storage types are supported in apasara.
    @RequestMapping("/restoreObject")
    public boolean restoreObject(@RequestParam String bucketName, @RequestParam String path, @RequestParam int days, @RequestParam String tier) {
        boolean result = storageObjectService.restoreObject(bucketName, path, days, tier);
//        boolean result = storageObjectService.restoreObject(BUCKET_NAME,
//                                                               "test-cold.html",
//                                                               1, "Expedited");
//        boolean result = storageObjectService.restoreObject(BUCKET_NAME,
//                                                               "test-deep-cold.html",
//                                                               1, "Expedited");
        logger.info("Restore object result is: {}", result);
        return result;
    }

    @RequestMapping("/deleteObject")
    public boolean deleteObject(@RequestParam String bucketName,  @RequestParam String objectPath) {
        boolean result = storageObjectService.deleteObject(bucketName, objectPath);
        logger.info("Delete object result is: {}", result);
        return result;
    }

//
    @RequestMapping("/deleteObjects")
    public boolean deleteObjects(@RequestParam String bucketName, @RequestParam String objects, @RequestParam(defaultValue = "false") boolean checkDeleteAll) {

        String[] list =  Arrays.stream(objects.split(","))
                .map(String::trim)
                .toArray(String[]::new);
        boolean result = storageObjectService.deleteObjects(bucketName, Arrays.asList(list), checkDeleteAll);
        logger.info("Delete objects result is: {}", result);
        return result;
    }

}