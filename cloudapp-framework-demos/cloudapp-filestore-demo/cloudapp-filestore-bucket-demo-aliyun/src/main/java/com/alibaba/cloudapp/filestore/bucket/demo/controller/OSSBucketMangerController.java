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

package com.alibaba.cloudapp.filestore.bucket.demo.controller;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.model.OSSObjectSummary;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
public class OSSBucketMangerController {

    private final static Logger logger = LoggerFactory.getLogger(OSSBucketMangerController.class);

    @Autowired
    BucketManager bucketManager;

    @Autowired
    StorageObjectService  storageObjectService;

    private String defaultBucketName = "test0610";

    @RequestMapping("/upload")
    public boolean upload() {
        try {
//            result = ossStorageObjectService.putObject(BUCKET_NAME, OBJECT_PATH, new ClassPathResource("test.txt").getInputStream(), "");
//            result = ossStorageObjectService.putObject(BUCKET_NAME, "test.html", new ClassPathResource("test.html").getInputStream(), "text/html");
            Boolean result = storageObjectService.putObject(defaultBucketName,
                    "test.txt",
                    new ClassPathResource(
                            "test.txt").getInputStream(),
                    ""
            );
            logger.info("Put object result is: {}", result);
            return result;
        } catch (IOException e) {
            logger.error("Error occurred when reading file", e);
            return false;
        }
    }

    @RequestMapping("/createBucket")
    public boolean createBucket(@RequestParam String bucketName) throws Exception {
        if (bucketName == null) {
            bucketName = this.defaultBucketName;
        }
        boolean createBucket = bucketManager.createBucket(bucketName);
        logger.info("Create bucket result : {}.", createBucket);
        return createBucket;
    }

    @RequestMapping("/getBucketLocation")
    public String getBucketLocation(String bucketName) throws Exception {
        if (bucketName == null) {
            bucketName = this.defaultBucketName;
        }
        String location = bucketManager.getBucketLocation(bucketName);
        logger.info("Bucket location: {}", location);
        return location;
    }

    //    TODO Error: com.aliyun.oss.OSSException: Bucket resource group feature is not supported in this region, in apasara.
    @RequestMapping("/listBucketsByBucketNamePrefix")
    public List listBuckets(@RequestParam String bucketNamePrefix, @RequestParam(value = "resourceGroupId", defaultValue = "") String resourceGroupId) throws Exception {
        List lists = bucketManager.listAllBucketsWithPrefix(bucketNamePrefix, "rg-xx");
        Optional.ofNullable(lists)
                .ifPresent(list -> list.forEach(item -> logger.info("Bucket: {}", item)));
        return lists;
    }

    //    TODO Error: com.aliyun.oss.OSSException: Bucket resource group feature is not supported in this region, in apasara.
    @RequestMapping("/listBucketByPagination")
    public List listBucketByPa(@RequestParam String prefix, @RequestParam(value = "resourceGroupId", defaultValue = "") String resourceGroupId, @RequestParam(required = false) String marker, @RequestParam(value = "bucketNum", defaultValue = "1") int bucketNum) throws Exception {
        Pagination pagination = new Pagination<com.aliyun.oss.model.Bucket>().builder()
                .maxResults(bucketNum)
                .build();
        Pagination result = bucketManager.listPagingBuckets(prefix, "rg-xx", pagination);
        Optional.ofNullable(result).ifPresent(list -> list.getDataList().forEach(item -> logger.info("Bucket: {}", item)));
        return result.getDataList();
    }


    @RequestMapping("/headObject")
    public String headObject(@RequestParam String bucketName, @RequestParam String objectPath) throws CloudAppException {
        if (bucketName == null) {
            bucketName = this.defaultBucketName;
        }
        ObjectMetadata objectMetadata = bucketManager.headObject(bucketName,
                objectPath
        );
        logger.info(objectPath);
        return "object metadata is : " + objectMetadata + ".";
    }

    @RequestMapping("/headObjectByVersionId")
    public String headObjectByVersionId(@RequestParam String versionId, @RequestParam String bucketName, @RequestParam String objectPath) throws CloudAppException {
        if (bucketName == null) {
            bucketName = this.defaultBucketName;
        }
//        versionId = "CAEQywEYgYDAhJOI140ZIiBhNTBjODFkZDU2YzM0ZmUxYmQzNjA0YmYxZGRmOTZlMg--";
        ObjectMetadata objectMetadata = bucketManager.headObject(bucketName,
                objectPath,
                versionId
        );
        logger.info(objectPath);
        return "object metadata is : " + objectMetadata + ".";
    }

    @RequestMapping("/listTopNObjects")
    public String listTopNObjects(@RequestParam String bucketName, @RequestParam(value = "topN", defaultValue = "1") int topN) throws CloudAppException {
        if (bucketName == null) {
            bucketName = this.defaultBucketName;
        }
        Collection<ObjectItem<OSSObjectSummary>> objectItems = bucketManager.listTopNObjects(
                bucketName, topN);
        objectItems.stream()
                .map(ObjectItem::getObjectName)
                .forEach(objectName -> logger.info("object name is: {}",
                        objectName
                ));
        return "object items are: " + JSONObject.toJSONString(objectItems) + ".";
    }

    @RequestMapping("/deleteBucket")
    public boolean deleteBucket(@RequestParam("bucketName") String bucketName) throws Exception {
        if (bucketName == null) {
            bucketName = this.defaultBucketName;
        }
        boolean deleteBucket = bucketManager.deleteBucket(bucketName);
        logger.info("Delete bucket result : {}.", deleteBucket);
        return deleteBucket;
    }

}

