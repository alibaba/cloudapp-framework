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
import com.alibaba.cloudapp.api.oauth2.TokenStorageService;
import com.alibaba.cloudapp.api.oauth2.handler.LoginHandler;
import com.alibaba.cloudapp.api.oauth2.verifier.TokenVerifier;
import com.alibaba.cloudapp.model.OAuth2Client;
import com.alibaba.cloudapp.oauth2.filter.OAuthCallbackFilter;
import com.alibaba.cloudapp.oauth2.filter.OAuthCheckLoginFilter;
import com.alibaba.cloudapp.oauth2.handler.DefaultLoginHandler;
import com.alibaba.cloudapp.oauth2.service.AuthorizationServiceImpl;
import com.alibaba.cloudapp.oauth2.service.DefaultTokenStorageService;
import com.alibaba.cloudapp.oauth2.verifier.IntrospectionTokenVerifier;
import com.alibaba.cloudapp.oauth2.verifier.JwtTokenVerifier;
import com.alibaba.cloudapp.oauth2.verifier.SimpleTokenVerifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@AutoConfiguration
@ConditionalOnClass(AuthorizationServiceImpl.class)
@ConditionalOnBean(OAuth2Client.class)
public class OAuth2AutoConfiguration {
    
    @Bean("oauthTemplate")
    @ConditionalOnMissingBean(name = "oauthTemplate")
    public RestTemplate oauth2Template() {
        return new RestTemplate();
    }
    
    @Bean("oauth2Component")
    @ConditionalOnMissingBean
    public OAuth2Component oauth2Component(OAuth2Client properties) {
        return new OAuth2Component(properties);
    }
    
    @Bean("authorizationService")
    @ConditionalOnMissingBean
    public AuthorizationService authorizationService(OAuth2Component component) {
        return component.getBean();
    }
    
    @Bean("storageToken")
    @ConditionalOnMissingBean
    public TokenStorageService storageToken() {
        return new DefaultTokenStorageService();
    }
    
    @Bean("tokenVerifier")
    @ConditionalOnMissingBean
    public TokenVerifier tokenVerifier(OAuth2Client properties) {
        if (properties.getIntrospectionUri() != null) {
            return new IntrospectionTokenVerifier(properties.getIntrospectionUri());
        } else if (properties.getJwksUrl() != null) {
            return new JwtTokenVerifier(properties.getJwksUrl());
        } else {
            return new SimpleTokenVerifier();
        }
    }
    
    @Bean("loginHandler")
    @ConditionalOnMissingBean
    public LoginHandler loginHandler(OAuth2Client properties) {
        String loginSuccessUrl = properties.getLoginSuccessUrl();
        if (!StringUtils.hasText(loginSuccessUrl)) {
            loginSuccessUrl = OAuthCallbackFilter.DEFAULT_SUCCESS_URL;
        }
        return new DefaultLoginHandler(loginSuccessUrl);
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({
            AuthorizationService.class,
            TokenStorageService.class,
    })
    public OAuthCallbackFilter oAuthCallbackInterceptor(
            AuthorizationService authorizationService,
            TokenStorageService storageToken,
            LoginHandler loginHandler
    ) {
        return new OAuthCallbackFilter(
                authorizationService, storageToken, loginHandler
        );
    }
    
    /**
     * grant_type: authorization_code | implicit
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({
            AuthorizationService.class,
            TokenStorageService.class,
            TokenVerifier.class
    })
    public OAuthCheckLoginFilter oAuthCheckLoginFilter(
            TokenStorageService storageToken,
            TokenVerifier tokenVerifier,
            AuthorizationService authorizationService,
            OAuth2Client properties
    ) {
        
        OAuthCheckLoginFilter filter = new OAuthCheckLoginFilter(
                storageToken, tokenVerifier, authorizationService
        );
        if(properties.getFilterSkipUrls() != null) {
            properties.getFilterSkipUrls().forEach(filter::addSkipUrls);
        }
        return filter;
    }
}
