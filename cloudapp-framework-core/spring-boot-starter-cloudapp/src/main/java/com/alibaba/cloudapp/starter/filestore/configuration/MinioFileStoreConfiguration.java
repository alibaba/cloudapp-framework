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

import com.alibaba.cloudapp.exeption.CloudAppInvalidAccessException;
import com.alibaba.cloudapp.filestore.minio.ClientProvider;
import com.alibaba.cloudapp.filestore.minio.service.*;
import com.alibaba.cloudapp.starter.filestore.properties.MinioEndpointProperties;
import com.alibaba.cloudapp.starter.properties.EnableModuleProperties;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(MinioClient.class)
@EnableModuleProperties(MinioEndpointProperties.class)
@ConditionalOnProperty(
        prefix = "io.cloudapp.filestore.minio",
        value = "enabled",
        havingValue = "true")
public class MinioFileStoreConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ClientProvider minioClient(MinioFileStoreComponent component) {
        return component.getBean();
    }
    
//    @OnRefresh(OSSEndpointProperties.class)
//    public void onRefresh(OSSEndpointProperties properties,
//                          MinioClient client) {
//        client.setCredentialsProvider(new StaticProvider(
//                properties.getAccessKey(),
//                properties.getSecretKey(),
//                properties.getStsToken()));
//    }

    @Bean("minioFileStoreComponent")
    @ConditionalOnMissingBean
    public MinioFileStoreComponent minioFileStoreComponent(MinioEndpointProperties properties)
            throws CloudAppInvalidAccessException {
        return new MinioFileStoreComponent(properties);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public MinioBucketManager minioBucketManager(ClientProvider minioClient) {
        return new MinioBucketManager(minioClient);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public MinioBucketLifeCycleManager minioBucketLifeCycleManager(ClientProvider minioClient) {
        return new MinioBucketLifeCycleManager(minioClient);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public MinioObjectPolicyManager minioObjectPolicyManager(ClientProvider minioClient) {
        return new MinioObjectPolicyManager(minioClient);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public MinioMultiPartsService minioMultiPartsService(ClientProvider minioClient) {
        return new MinioMultiPartsService(minioClient);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public MinioStorageObjectService minioStorageObjectService(ClientProvider minioClient) {
        return new MinioStorageObjectService(minioClient);
    }
    
}
