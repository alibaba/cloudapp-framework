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

import com.alibaba.cloudapp.model.OAuth2Client;
import com.alibaba.cloudapp.oauth2.service.AuthorizationServiceImpl;
import com.alibaba.cloudapp.starter.apigateway.properties.ApiGatewayProperties;
import com.alibaba.cloudapp.starter.oauth2.configuration.OAuth2AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass({AuthorizationServiceImpl.class, ApiGatewayProperties.class})
@AutoConfigureBefore(OAuth2AutoConfiguration.class)
public class ApiGatewayOAuth2Configuration {
    
    @Bean("oauth2Client")
    @ConditionalOnMissingBean
    @ConditionalOnExpression("#{environment.containsProperty(" +
                "'io.cloudapp.apigateway.aliyun.oAuth2.clientId'" +
            ") || environment.containsProperty(" +
                "'io.cloudapp.apigateway.aliyun.oAuth2.client-id'" +
            ")}")
    public OAuth2Client oauth2Client(ApiGatewayProperties properties) {
        return properties.getOAuth2();
    }
    
}
