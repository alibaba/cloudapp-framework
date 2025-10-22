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
package com.alibaba.cloudapp.starter.filestore.configuration;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.alibaba.cloudapp.exeption.CloudAppInvalidAccessException;
import com.alibaba.cloudapp.filestore.aliyun.service.*;
import com.alibaba.cloudapp.starter.filestore.properties.OSSEndpointProperties;
import com.alibaba.cloudapp.starter.properties.EnableModuleProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

@AutoConfiguration
@ConditionalOnClass(OSSClient.class )
@EnableModuleProperties(OSSEndpointProperties.class)
@ConditionalOnProperty(
        prefix = AliyunFileStoreComponent.BINDING_PROP_KEY,
        value = "enabled",
        havingValue = "true")
public class AliyunFileStoreConfiguration {

    @Bean("ossClient")
    @ConditionalOnMissingBean
    public OSS ossClient(AliyunFileStoreComponent fileStoreComponent) {
        return fileStoreComponent.getBean();
    }

    @Bean("fileStoreComponent")
    @ConditionalOnMissingBean
    public AliyunFileStoreComponent fileStoreComponent(OSSEndpointProperties properties)
            throws CloudAppInvalidAccessException {

        if(!StringUtils.hasText(properties.getAccessKey())
                || !StringUtils.hasText(properties.getSecretKey())) {
            throw new CloudAppInvalidAccessException("AccessKey or SecretKey must not be empty");
        }

        return new AliyunFileStoreComponent(properties);
    }

    @Bean("ossBucketLifeCycleManager")
    @ConditionalOnMissingBean
    public OSSBucketLifeCycleManager ossBucketLifeCycleManager(OSS ossClient) {
        return new OSSBucketLifeCycleManager(ossClient);
    }

    @Bean("ossBucketManager")
    @ConditionalOnMissingBean
    public OSSBucketManager ossBucketManager(OSS ossClient) {
        return new OSSBucketManager(ossClient);
    }

    @Bean("ossBucketPolicyManager")
    @ConditionalOnMissingBean
    public OSSObjectPolicyManager ossBucketPolicyManager(OSS ossClient) {
        return new OSSObjectPolicyManager(ossClient);
    }

    @Bean("ossMultiPartsService")
    @ConditionalOnMissingBean
    public OSSMultiPartsService ossMultiPartsService(OSS ossClient) {
        return new OSSMultiPartsService(ossClient);
    }

    @Bean("ossStorageObjectService")
    @ConditionalOnMissingBean
    public OSSStorageObjectService ossStorageObjectService(OSS ossClient) {
        return new OSSStorageObjectService(ossClient);
    }

}
