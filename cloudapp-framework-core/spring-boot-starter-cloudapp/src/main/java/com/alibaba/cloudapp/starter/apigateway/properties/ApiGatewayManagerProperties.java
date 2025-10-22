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

package com.alibaba.cloudapp.starter.apigateway.properties;

import com.alibaba.cloudapp.starter.apigateway.configuration.ApiGatewayManagerRefreshComponent;
import com.alibaba.cloudapp.starter.base.properties.RefreshableProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = ApiGatewayManagerRefreshComponent.BIND_KEY)
public class ApiGatewayManagerProperties extends RefreshableProperties {

    private String accessKey;
    private String secretKey;
    private String gatewayUri;
    private String regionId;
    private String resourceGroupId;
    private String organizationId;
    private String productCode;
    private String sdkSource;
    private boolean ssl = false;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getGatewayUri() {
        return gatewayUri;
    }

    public void setGatewayUri(String gatewayUri) {
        this.gatewayUri = gatewayUri;
    }
    
    public String getRegionId() {
        return regionId;
    }
    
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
    
    public String getResourceGroupId() {
        return resourceGroupId;
    }
    
    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }
    
    public String getOrganizationId() {
        return organizationId;
    }
    
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    
    public boolean isSsl() {
        return ssl;
    }
    
    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public String getSdkSource() {
        return sdkSource;
    }
    
    public void setSdkSource(String sdkSource) {
        this.sdkSource = sdkSource;
    }
    
}
