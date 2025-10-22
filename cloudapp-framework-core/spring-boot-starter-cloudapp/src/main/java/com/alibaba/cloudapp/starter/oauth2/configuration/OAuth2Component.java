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

package com.alibaba.cloudapp.starter.oauth2.configuration;

import com.alibaba.cloudapp.api.oauth2.AuthorizationService;
import com.alibaba.cloudapp.api.oauth2.GrantType;
import com.alibaba.cloudapp.model.OAuth2Client;
import com.alibaba.cloudapp.oauth2.service.AuthorizationServiceImpl;
import com.alibaba.cloudapp.starter.base.RefreshableComponent;
import org.springframework.util.StringUtils;

import java.util.List;

public class OAuth2Component extends RefreshableComponent<OAuth2Client,
        AuthorizationService> {
    
    public static final String BINDING_PROP_KEY = "io.cloudapp.oauth2";
    
    public OAuth2Component(OAuth2Client properties) {
        super(properties);
    }
    
    @Override
    public void postStart() {
        if (!StringUtils.hasText(properties.getClientId())
                || !StringUtils.hasText(properties.getClientSecret())) {
            throw new IllegalArgumentException(
                    "ClientId and ClientSecret must be provided");
        }
        List<String> grantTypes = properties.getGrantTypes();
        
        if (grantTypes == null || grantTypes.isEmpty()) {
            throw new IllegalArgumentException("GrantTypes must be provided");
        }
        
        if (properties.getScopes() == null || properties.getScopes()
                                                        .isEmpty()) {
            throw new IllegalArgumentException("Scopes must be provided");
        }
        
        if ((grantTypes.contains(GrantType.AUTHORIZATION_CODE.getGrantType())
                || grantTypes.contains(GrantType.IMPLICIT.getGrantType()))
                && !StringUtils.hasText(properties.getRedirectUri())) {
            throw new IllegalArgumentException("RedirectUri must be provided");
        }
        
        
        bean.setOAuth2Client(properties);
    }
    
    @Override
    public void preStop() {
    
    }
    
    @Override
    public String bindKey() {
        return BINDING_PROP_KEY;
    }
    
    @Override
    public String getName() {
        return "cloudappAuthorizationService";
    }
    
    @Override
    protected AuthorizationService createBean(OAuth2Client properties) {
        return new AuthorizationServiceImpl(properties);
    }
    
}
