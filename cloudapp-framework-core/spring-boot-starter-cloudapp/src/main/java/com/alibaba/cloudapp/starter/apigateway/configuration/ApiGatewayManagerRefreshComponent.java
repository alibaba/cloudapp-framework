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

package com.alibaba.cloudapp.starter.apigateway.configuration;

import com.alibaba.cloudapp.apigateway.aliyun.service.ApiGatewayManager;
import com.alibaba.cloudapp.starter.apigateway.properties.ApiGatewayManagerProperties;
import com.alibaba.cloudapp.starter.base.RefreshableComponent;
import org.springframework.util.StringUtils;

public class ApiGatewayManagerRefreshComponent extends
        RefreshableComponent<ApiGatewayManagerProperties, ApiGatewayManager> {
    
    public static final String BIND_KEY = "io.cloudapp.apigateway.aliyun.server";
    
    public ApiGatewayManagerRefreshComponent(ApiGatewayManagerProperties properties) {
        super(properties);
    }
    
    @Override
    public void postStart() {
        bean.setAccessKey(properties.getAccessKey());
        bean.setSecretKey(properties.getSecretKey());
        bean.setEndpoint(properties.getGatewayUri());
        bean.setRegionId(properties.getRegionId());
        bean.setResourceGroupId(properties.getResourceGroupId());
        bean.setOrganizationId(properties.getOrganizationId());
        bean.setSsl(properties.isSsl());
        bean.setProductCode(properties.getProductCode());
        bean.setSdkSource(properties.getSdkSource());
        bean.refresh();
    }
    
    @Override
    public void preStop() {
    
    }
    
    @Override
    public String bindKey() {
        return BIND_KEY;
    }
    
    @Override
    public String getName() {
        return "apiGateway.manager";
    }
    
    
    @Override
    protected ApiGatewayManager createBean(ApiGatewayManagerProperties properties) {
        ApiGatewayManager manamger = new ApiGatewayManager(
                properties.getAccessKey(),
                properties.getSecretKey(),
                properties.getGatewayUri(),
                properties.getRegionId(),
                properties.getResourceGroupId(),
                properties.getOrganizationId(),
                properties.isSsl()
        );
        if(StringUtils.hasText(properties.getProductCode())) {
            manamger.setProductCode(properties.getProductCode());
        }
        if(StringUtils.hasText(properties.getSdkSource())) {
            manamger.setSdkSource(properties.getSdkSource());
        }
        return manamger;
    }
    
    
}
