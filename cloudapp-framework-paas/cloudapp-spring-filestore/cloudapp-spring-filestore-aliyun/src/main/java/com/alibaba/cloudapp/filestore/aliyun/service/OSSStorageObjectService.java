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
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.alibaba.cloudapp.api.filestore.StorageObjectService;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.filestore.aliyun.util.OSSExceptionConverter;
import com.alibaba.cloudapp.filestore.aliyun.util.OSSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OSSStorageObjectService implements StorageObjectService<OSS> {

    private static final Logger logger = LoggerFactory.getLogger(OSSStorageObjectService.class);

    private final OSS oss;

    public OSSStorageObjectService(OSS oss) {
        this.oss = oss;
    }

    @Override
    public OSS getDelegatingStorageClient() {
        return oss;
    }

    @Override
    public boolean copy(String sourceBucket, String sourcePath, String targetBucket,
                        String targetPath, boolean override)
            throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, sourceBucket);
            
            OSSUtil.checkBucketExists(oss, targetBucket);

            CopyObjectRequest request = new CopyObjectRequest(
                    sourceBucket, sourcePath, targetBucket, targetPath);

            if(!override && oss.doesObjectExist(sourceBucket, targetPath)) {
                throw new CloudAppException("Target already exists",
                        "CloudApp.OSSTargetExisted");
            }

            CopyObjectResult result = oss.copyObject(request);

            if(logger.isDebugEnabled()) {
                logger.debug("Copied object {} finished at {}",
                        result.getETag(), result.getLastModified());
            }

            return true;
        } catch (Exception e) {
            logger.error("Error copying object, " +
                    "bucketName: {}, sourcePath: {}, targetPath: {}",
                    sourceBucket, sourcePath, targetPath);

            throw OSSExceptionConverter.convert(e);
        }
    }

    @Override
    public boolean deleteObject(String bucketName, String path) throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            VoidResult result = oss.deleteObject(bucketName, path);

            if (logger.isDebugEnabled()) {
                logger.debug("Deleted bucket: {} path: {} object: {}",
                        bucketName, path, JSON.toJSONString(result));
            }

            return result.getResponse() == null || result.getResponse().isSuccessful();
        } catch (Exception e) {
            logger.error("Error deleting bucket: {} path: {} object",
                    bucketName, path);

            throw OSSExceptionConverter.convert(e);
        }
    }

    @Override
    public boolean deleteObjects(String bucketName, Collection<String> objects, boolean checkAllDeleted)
            throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName);
            request.setKeys(new ArrayList<>(objects));

            DeleteObjectsResult result = oss.deleteObjects(request);

            if (logger.isDebugEnabled()) {
                logger.debug("Deleted objects: {}",  JSON.toJSONString(result));
            }

            int deletedSize = result.getDeletedObjects().size();
            int objectsSize = objects.size();

            if (deletedSize != objectsSize) {
                logger.warn("Deleted objects size: {} , objects: {}",
                        deletedSize,
                        result.getDeletedObjects());
            }

            return !checkAllDeleted || deletedSize == objectsSize;
        } catch (Exception e) {
            logger.error("Error deleting objects, bucketName: {}", bucketName);

            throw OSSExceptionConverter.convert(e);
        }
    }

    @Override
    public InputStream getObject(String bucketName, String path) throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            GetObjectRequest request = new GetObjectRequest(bucketName, path);

            OSSObject object = oss.getObject(request);

            return object.getObjectContent();
        } catch (Exception e) {
            logger.error("Error getting object, bucket: {} path: {}",
                    bucketName, path);

            throw OSSExceptionConverter.convert(e);
        }
    }

    @Override
    public boolean putObject(String bucketName, String path, InputStream body, String contentType)
            throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            PutObjectRequest request = new PutObjectRequest(bucketName, path, body);

            if(StringUtils.hasText(contentType)) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(contentType);
                request.setMetadata(metadata);
            }

            PutObjectResult result = oss.putObject(request);

            if (logger.isDebugEnabled()) {
                logger.debug("Put object: {}, requestId: {}", result.getETag(), result.getRequestId());
            }

            return result.getResponse() == null || result.getResponse().isSuccessful();
        } catch (Exception e) {
            logger.error("Error putting object, bucket: {} path {}", bucketName, path);
            throw OSSExceptionConverter.convert(e);
        }
    }

    @Override
    public Collection<String> listObjectVersions(String bucketName, String path, String sinceVersion, int count)
            throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            ListVersionsRequest request = new ListVersionsRequest();
            request.setBucketName(bucketName);
//            request.setKey(path);
            request.setKeyMarker(path);
            request.setMaxResults(count);
            request.setVersionIdMarker(sinceVersion);
            request.setPrefix(path);

            // this api returns a list of all object versions
            VersionListing listing = oss.listVersions(request);

            if (logger.isDebugEnabled()) {
                logger.debug("Listed object versions:  {}", JSON.toJSONString(listing));
            }

            if(listing.getVersionSummaries() == null) {
                return null;
            }

            List<String> list = new ArrayList<>();

            for (OSSVersionSummary versionSummary : listing.getVersionSummaries()) {
                if(sinceVersion != null && sinceVersion.equals(versionSummary.getVersionId())) {
                    break;
                }
                list.add(versionSummary.getVersionId());
            }

            return list;
        } catch (Exception e) {
            logger.error("Error listing object versions, bucket: {} path {}", bucketName, path);
            throw OSSExceptionConverter.convert(e);
        }
    }

    @Override
    public boolean restoreObject(String bucketName, String path, int days, String tier) throws CloudAppException {
        try {
            OSSUtil.checkBucketExists(oss, bucketName);

            RestoreConfiguration configuration = null;

            if(StringUtils.hasText(tier)) {
                RestoreTier restoreTier = RestoreTier.parse(tier);
                configuration = new RestoreConfiguration(days, new RestoreJobParameters(restoreTier));
            }

            RestoreObjectResult result = oss.restoreObject(bucketName, path, configuration);

            if (logger.isDebugEnabled()) {
                logger.debug("Restored object: {}", JSON.toJSONString(result));
            }
            return result.getResponse() == null || result.getResponse().isSuccessful();
        } catch (Exception e) {
            logger.error("Error restoring object, bucket: {} path {}", bucketName, path);
            throw OSSExceptionConverter.convert(e);
        }
    }
}
