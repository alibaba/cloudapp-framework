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

package com.alibaba.cloudapp.filestore.aliyun.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.TagSet;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class OSSStorageObjectServiceSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            OSSStorageObjectServiceSit.class);
    private static final String BUCKET_NAME = "testoss987";
    private static final String OBJECT_PATH = "test.txt";
    
    @Autowired
    @Qualifier("ossStorageObjectService")
    private OSSStorageObjectService ossStorageObjectService;
    
    @Before
    public void init() throws InterruptedException {
        boolean bucketExist = ossStorageObjectService.getDelegatingStorageClient()
                                                     .doesBucketExist(
                                                             BUCKET_NAME);
        
        if (!bucketExist) {
            ossStorageObjectService.getDelegatingStorageClient().createBucket(
                    BUCKET_NAME);
            Thread.sleep(3000);
            logger.info("Bucket {} is not exist,creating", BUCKET_NAME);
        }
    }
    
    @Test
    public void testCopy() throws CloudAppException {
        String targetPath = "dirbyapi/test2.txt";
        String targetBucket = "testoss987";
//        boolean result = ossStorageObjectService.copy(BUCKET_NAME, OBJECT_PATH, targetPath, false);
        boolean result = ossStorageObjectService.copy(BUCKET_NAME, OBJECT_PATH, targetBucket,
                                                      targetPath, true
        );
        logger.info("Copy object result is: {}", result);
    }
    
    @Test
    public void testDeleteObject() throws CloudAppException {
        boolean result = ossStorageObjectService.deleteObject(BUCKET_NAME,
                                                              "dirbyapi/test.txt"
        );
        logger.info("Delete object result is: {}", result);
    }
    
    @Test
    public void testDeleteObjects() throws CloudAppException {
        Collection<String> objects = new ArrayList<>();
        objects.add("dirbyapi/test.txt");
        objects.add("dirbyapi/test2.txt");
        objects.add("dirbyapi/test3.txt");
        boolean result = ossStorageObjectService.deleteObjects(BUCKET_NAME,
                                                               objects, true
        );
        logger.info("Delete objects result is: {}", result);
    }
    
    @Test
    public void testGetObject() throws CloudAppException {
        InputStream object = ossStorageObjectService.getObject(BUCKET_NAME,
                                                               OBJECT_PATH
        );
        Path testResourcesDir = Paths.get(System.getProperty("user.dir"))
                                     .resolve("src")
                                     .resolve("test")
                                     .resolve("resources");
        try (OutputStream outputStream = Files.newOutputStream(
                Paths.get(testResourcesDir.resolve("test.txt").toString()))) {
            byte[] buffer = new byte[4096];
            int n;
            while ((n = object.read(buffer)) != -1) {
                outputStream.write(buffer, 0, n);
            }
        } catch (IOException e) {
            logger.error("Error occurred when writing file to disk", e);
        }
    }
    
    
    @Test
    public void testPutObject() throws CloudAppException {
        boolean result = false;
        
        try {
//            result = ossStorageObjectService.putObject(BUCKET_NAME, OBJECT_PATH, new ClassPathResource("test.txt").getInputStream(), "");
//            result = ossStorageObjectService.putObject(BUCKET_NAME, "test.html", new ClassPathResource("test.html").getInputStream(), "text/html");
            result = ossStorageObjectService.putObject(BUCKET_NAME,
                                                       "heishenhua.jpg",
                                                       new ClassPathResource(
                                                               "heishenhua.jpg").getInputStream(),
                                                       ""
            );
        } catch (IOException e) {
            logger.error("Error occurred when reading file", e);
        }
        logger.info("Put object result is: {}", result);
    }
    
    @Test
    public void testRestoreObject() throws CloudAppException {
        boolean result = ossStorageObjectService.restoreObject(BUCKET_NAME,
                                                               "test.html",
                                                               1, null
        );
//        boolean result = ossStorageObjectService.restoreObject(BUCKET_NAME,
//                                                               "test-cold.html",
//                                                               1, "Expedited");
//        boolean result = ossStorageObjectService.restoreObject(BUCKET_NAME,
//                                                               "test-deep-cold.html",
//                                                               1, "Expedited");
        logger.info("Restore object result is: {}", result);
    }
    
    @Test
    public void testListObjectVersions() throws CloudAppException {
        Collection<String> versions = ossStorageObjectService.listObjectVersions(
                BUCKET_NAME, OBJECT_PATH,
                "CAEQywEYgYCAj4qz140ZIiAyZWE1OTBmYjM1MzY0Yzc3YWEzYjAyZmFmYzUyNDYzMw--",
                10
        );
        logger.info("List object versions result is: {}", versions);
    }
    
    @Test
    public void testGetObjectTag() throws CloudAppException {
        OSS oss = ossStorageObjectService.getDelegatingStorageClient();
        TagSet tagSet = oss.getObjectTagging(BUCKET_NAME, OBJECT_PATH);
        tagSet.getAllTags().forEach((key, value) -> logger.info(
                "Object {} tag key is: {}, value is: {}", OBJECT_PATH, key,
                value
        ));
    }
    
    
}
