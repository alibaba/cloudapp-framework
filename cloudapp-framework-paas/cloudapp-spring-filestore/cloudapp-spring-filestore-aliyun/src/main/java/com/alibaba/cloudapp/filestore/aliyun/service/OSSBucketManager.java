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

import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.*;
import com.alibaba.cloudapp.api.common.Pagination;
import com.alibaba.cloudapp.api.filestore.BucketManager;
import com.alibaba.cloudapp.api.filestore.model.Bucket;
import com.alibaba.cloudapp.api.filestore.model.ObjectItem;
import com.alibaba.cloudapp.api.filestore.model.ObjectMetadata;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.filestore.aliyun.OSSConstant;
import com.alibaba.cloudapp.filestore.aliyun.util.OSSExceptionConverter;
import com.alibaba.cloudapp.filestore.aliyun.util.OSSUtil;
import com.alibaba.cloudapp.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OSSBucketManager implements
        BucketManager<OSS,
                com.aliyun.oss.model.Bucket,
                ObjectMetadata,
                OSSObjectSummary> {

    private static final Logger logger = LoggerFactory.getLogger(OSSBucketManager.class);

    private final OSS oss;

    public OSSBucketManager(OSS oss) {
        this.oss = oss;
    }

    @Override
    public OSS getDelegatingStorageClient() {
        return this.oss;
    }

    @Override
    public List<Bucket<com.aliyun.oss.model.Bucket>>
    listAllBucketsWithPrefix(String prefix, String resourceGroupId)
            throws CloudAppException{
        try {
            ListBucketsRequest request = new ListBucketsRequest();
            request.setPrefix(prefix);
//            request.setResourceGroupId(resourceGroupId); //apasara not support
            request.setMaxKeys(OSSConstant.MAX_PAGE_SIZE);

            BucketList buckets;

            List<Bucket<com.aliyun.oss.model.Bucket>> bucketList = new ArrayList<>();

            do {
                buckets = oss.listBuckets(request);

                if(buckets == null || buckets.getBucketList() == null) {
                    break;
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("listAllBucketsWithPrefix with prefix: {}, " +
                                    "returned listed buckets: {}, " +
                                    "isTruncated:{} (true will fetch next page)",
                            prefix,
                            buckets.getBucketList().size(),
                            buckets.isTruncated());
                }

                bucketList.addAll(buckets
                        .getBucketList()
                        .stream()
                        .map(this::convertToBucket)
                        .collect(Collectors.toList())
                );

                request.setMarker(buckets.getNextMarker());

            } while (buckets.isTruncated());

            return bucketList;
        } catch (ClientException ex) {
            logger.error("the error occurred when listing buckets");

            throw OSSExceptionConverter.convert(ex);
        }
    }

    @Override
    public boolean createBucket(String bucketName) throws CloudAppException {
        try {
            CreateBucketRequest request = new CreateBucketRequest(bucketName);

            com.aliyun.oss.model.Bucket ossBucket = oss.createBucket(request);

            return ossBucket.getRequestId() != null;
        } catch (ClientException ex) {
            logger.error("the error occurred when creating bucket");
            throw OSSExceptionConverter.convert(ex);
        }
    }

    @Override
    public Pagination<Bucket<com.aliyun.oss.model.Bucket>> listPagingBuckets(
            String prefix,
            String resourceGroupId,
            Pagination paging) throws CloudAppException {
        try {

            validatePaging(paging);

            ListBucketsRequest request = new ListBucketsRequest();
            request.setPrefix(prefix);
//            request.setResourceGroupId(resourceGroupId); // apasara not support
            request.setMaxKeys(paging.getMaxResults());
            request.setMarker(paging.getNextToken());

            BucketList buckets = oss.listBuckets(request);

            List<Bucket<com.aliyun.oss.model.Bucket>> bucketList =
                    buckets.getBucketList() == null ?
                            Collections.emptyList():
                            buckets.getBucketList()
                                    .stream()
                                    .map(this::convertToBucket)
                                    .collect(Collectors.toList());

            return new Pagination<Bucket<com.aliyun.oss.model.Bucket>>().builder()
                    .dataList(bucketList)
                    .hasNext(buckets.isTruncated())
                    .maxResults(buckets.getMaxKeys())
                    .nextToken(buckets.getNextMarker())
                    .build();

        } catch (ClientException e) {
            logger.error("the error occurred when listPagingBuckets, " +
                    "prefix: {}, resourceGroupId: {}",
                    prefix, resourceGroupId);

            throw OSSExceptionConverter.convert(e);
        }
    }

    private static void validatePaging(Pagination paging) {
        int size = paging.getMaxResults();
        int min = OSSConstant.MIN_PAGE_SIZE;
        int max = OSSConstant.MAX_PAGE_SIZE;

        if ( size < min || size > max)  {
            throw new CloudAppException(
                    "MaxResults must be between " + min + " and " + max,
                    "CloudApp.InvalidPagingSize");
        }
    }

    @Override
    public Bucket<com.aliyun.oss.model.Bucket> createBucket(
            Bucket<com.aliyun.oss.model.Bucket> bucket)
            throws CloudAppException {
        try {
            CreateBucketRequest request = new CreateBucketRequest(bucket.getName());

            com.aliyun.oss.model.Bucket ossBucket = bucket.getDelegatingBucket();

            request.setLocationConstraint(ossBucket.getLocation());
            request.setHnsStatus(ossBucket.getHnsStatus());
            request.setResourceGroupId(ossBucket.getResourceGroupId());
            request.setStorageClass(ossBucket.getStorageClass());

            ossBucket = oss.createBucket(request);

            return convertToBucket(ossBucket);
        } catch (ClientException ex) {
            logger.error("the error occurred when creating bucket");

            throw OSSExceptionConverter.convert(ex);
        }
    }

    @Override
    public boolean deleteBucket(String bucketName) throws CloudAppException {
        try {
            VoidResult result = oss.deleteBucket(bucketName);

            if (logger.isDebugEnabled()) {
                logger.debug("deleteBucket return: " + JSON.toJSONString(result));
            }

            ResponseMessage res = result.getResponse();
            boolean success = res != null && res.isSuccessful();

            if (!success) {
                logger.warn("Deleting bucket failed for bucket: {}, " +
                        "as res failed: {}",
                        bucketName, res == null ? "null" : "yes");
            }

            return success;
        } catch (ClientException ex) {
            logger.error("the error occurred when deleting bucket");

            throw OSSExceptionConverter.convert(ex);
        }
    }

    @Override
    public String getBucketLocation(String bucketName) throws CloudAppException {
        try {
            return oss.getBucketLocation(bucketName);
        } catch (ClientException ex) {
            logger.error("the error occurred when getting bucket location," +
                    " bucketName: {}", bucketName);
            throw OSSExceptionConverter.convert(ex);
        }
    }

    @Override
    public ObjectMetadata headObject(String bucketName, String path) throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            HeadObjectRequest request = new HeadObjectRequest(bucketName, path);
            com.aliyun.oss.model.ObjectMetadata metadata = oss.headObject(request);

            return ObjectMetadata.builder()
                    .etag(metadata.getETag())
                    .size((int) metadata.getContentLength())
                    .delegatingMetadata(metadata)
                    .build();

        } catch (ClientException ex) {
            logger.error("the error occurred when getting object metadata," +
                    " bucketName: {}, path: {}", bucketName, path);
            throw OSSExceptionConverter.convert(ex);
        }
    }

    @Override
    public ObjectMetadata headObject(String bucketName,
                                     String path,
                                     String versionId) throws CloudAppException {
        try {

            OSSUtil.checkBucketExists(oss, bucketName);

            HeadObjectRequest request = new HeadObjectRequest(bucketName, path, versionId);
            com.aliyun.oss.model.ObjectMetadata metadata = oss.headObject(request);

            return ObjectMetadata.builder()
                    .etag(metadata.getETag())
                    .size(0)
                    .delegatingMetadata(metadata)
                    .build();

        } catch (ClientException ex) {
            String message = String.format(
                    "the error occurred when getting object metadata, " +
                    "buketName: %s, path: %s, versionId: %s",
                    bucketName, path, versionId);

            throw new CloudAppException(message, ex);
        }

    }

    @Override
    public Collection<ObjectItem<OSSObjectSummary>> listTopNObjects(String bucketName, int count) throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            int loopCount = (count + OSSConstant.MAX_PAGE_SIZE - 1) / OSSConstant.MAX_PAGE_SIZE;
            ListObjectsRequest request = new ListObjectsRequest(bucketName);

            request.setMaxKeys(Math.min(OSSConstant.MAX_PAGE_SIZE, count));

            List<ObjectItem<OSSObjectSummary>> result = new ArrayList<>(count);

            ObjectListing listing;
            do {
                listing = oss.listObjects(request);

                if(listing.getObjectSummaries() != null) {
                    result.addAll(listing
                            .getObjectSummaries()
                            .stream()
                            .map(this::convertToObjectItem)
                            .collect(Collectors.toList()));
                }

                request.setMarker(listing.getNextMarker());
                loopCount--;

            } while (loopCount > 0 && listing.isTruncated());

            return result;
        } catch (ClientException ex) {
            logger.error("the error occurred when listTopNObjects, " +
                    "bucketName: {}, count: {}",
                    bucketName, count);

            throw OSSExceptionConverter.convert(ex);
        }
    }

    @Override
    public Pagination<ObjectItem<OSSObjectSummary>> listObjects(
            String bucketName, Pagination pageMarker)
            throws CloudAppException {
        try{
            OSSUtil.checkBucketExists(oss, bucketName);

            ListObjectsRequest request = new ListObjectsRequest(bucketName);
            request.setMarker(pageMarker.getNextToken());
            request.setMaxKeys(pageMarker.getMaxResults());
            request.setMaxKeys(pageMarker.getMaxResults());

            ObjectListing objectListing = oss.listObjects(request);

            List<ObjectItem<OSSObjectSummary>> objectList =
                    objectListing.getObjectSummaries() != null ?
                    objectListing.getObjectSummaries()
                            .stream()
                            .map(this::convertToObjectItem)
                            .collect(Collectors.toList())
                            : Collections.emptyList();

            return new Pagination<ObjectItem<OSSObjectSummary>>().builder()
                    .dataList(objectList)
                    .hasNext(objectListing.isTruncated())
                    .maxResults(objectListing.getMaxKeys())
                    .nextToken(objectListing.getNextMarker())
                    .build();

        } catch (ClientException ex) {
            logger.error("the error occurred when list objects, bucketName: {}",
                    bucketName);

            throw OSSExceptionConverter.convert(ex);
        }
    }

    private ObjectItem<OSSObjectSummary> convertToObjectItem(OSSObjectSummary e) {
        return ObjectItem.<OSSObjectSummary>builder()
                .delegatingMetadata(e)
                .objectName(e.getKey())
                .size(e.getSize())
                .etag(e.getETag())
                .lastModified(e.getLastModified())
                .build();
    }

    private Bucket<com.aliyun.oss.model.Bucket> convertToBucket(com.aliyun.oss.model.Bucket e) {
        return Bucket.<com.aliyun.oss.model.Bucket>builder()
                .creationDate(DateUtil.formatDate(e.getCreationDate(), DateUtil.TIMESTAMP_FORMAT))
                .name(e.getName())
                .region(e.getRegion())
                .delegatingBucket(e)
                .build();
    }

}
