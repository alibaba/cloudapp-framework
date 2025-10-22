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

import com.alibaba.cloudapp.apigateway.aliyun.AuthTypeEnum;
import com.alibaba.cloudapp.apigateway.aliyun.properties.*;
import com.alibaba.cloudapp.model.OAuth2Client;
import com.alibaba.cloudapp.starter.apigateway.configuration.AuthorizedRefreshComponent;
import com.alibaba.cloudapp.starter.base.properties.RefreshableProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = AuthorizedRefreshComponent.BINDING_KEY)
public class ApiGatewayProperties extends RefreshableProperties implements Config {

    private ApiKeyProperties apiKey;
    private JwtProperties jwt;
    private OAuth2Client oAuth2;
    private BasicProperties basic;

    @Override
    public AuthTypeEnum getAuthType() {
        if(apiKey != null) {
            return AuthTypeEnum.APIKEY;
        }
        if(basic != null) {
            return AuthTypeEnum.BASIC;
        }
        if(jwt != null) {
            return AuthTypeEnum.JWT;
        }
        if(oAuth2 != null) {
            return AuthTypeEnum.OAUTH2;
        }
        return AuthTypeEnum.NO_AUTH;
    }

    @Override
    public ApiKeyProperties getApiKey() {
        return apiKey;
    }

    @Override
    public JwtProperties getJwt() {
        return jwt;
    }

    @Override
    public OAuth2Client getOAuth2() {
        return oAuth2;
    }

    @Override
    public BasicProperties getBasic() {
        return basic;
    }

    public void setApiKey(ApiKeyProperties apiKey) {
        this.apiKey = apiKey;
    }

    public void setJwt(JwtProperties jwt) {
        this.jwt = jwt;
    }

    public void setOAuth2(OAuth2Client oAuth2) {
        this.oAuth2 = oAuth2;
    }

    public void setBasic(BasicProperties basic) {
        this.basic = basic;
    }

}
