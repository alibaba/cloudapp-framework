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

package com.alibaba.cloudapp.apigateway.aliyun.service;

import com.alibaba.cloudapp.api.gateway.Authorized;
import com.alibaba.cloudapp.api.gateway.authentication.*;
import com.alibaba.cloudapp.api.gateway.model.JWTParams;
import com.alibaba.cloudapp.api.oauth2.AuthorizationService;
import com.alibaba.cloudapp.api.oauth2.TokenStorageService;
import com.alibaba.cloudapp.api.oauth2.verifier.TokenVerifier;
import com.alibaba.cloudapp.apigateway.aliyun.ApiGatewayConstant;
import com.alibaba.cloudapp.apigateway.aliyun.AuthTypeEnum;
import com.alibaba.cloudapp.apigateway.aliyun.properties.ApiKeyProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.BasicProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.Config;
import com.alibaba.cloudapp.apigateway.aliyun.properties.JwtProperties;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.model.OAuth2Client;
import com.alibaba.cloudapp.oauth2.service.AuthorizationServiceImpl;
import com.alibaba.cloudapp.oauth2.verifier.IntrospectionTokenVerifier;
import com.alibaba.cloudapp.oauth2.verifier.JwtTokenVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collections;

public class AuthorizedService implements Authorized, InitializingBean {
    
    private static final Logger logger = LoggerFactory.getLogger(
            AuthorizedService.class);
    
    private Config properties;
    private Authorizer authorizer;
    
    @Autowired(required = false)
    private TokenStorageService storageService;

    @Autowired(required = false)
    private AuthorizationService authorizationService;
    
    public AuthorizedService(Authorizer authorizer) {
        this.authorizer = authorizer;
        this.properties = null;
    }
    
    public AuthorizedService(Config properties) {
        this.properties = properties;
    }
    
    public Authorizer initAuthorizer() throws CloudAppException {
        Assert.notNull(properties, "ApiGatewayProperties must not be null");
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing ApiGatewayProperties.");
        }
        
        AuthTypeEnum type = properties.getAuthType();
        if (type == null) {
            throw new CloudAppException("CloudApp.InvalidAuthType");
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("auth type: " + type.getName());
        }
        
        switch (type) {
            case APIKEY:
                return createApiKeyAuthorizer(properties.getApiKey());
            case BASIC:
                return createBasicAuthorizer(properties.getBasic());
            case JWT:
                return createJwtAuthorizer(properties.getJwt());
            case OAUTH2:
                return createoAuth2Authorizer(properties.getOAuth2());
            default:
                return new NoAuthorization();
        }
    }
    
    /**
     * create oauth2 authorizer
     *
     * @return OAuth2Authorizer
     */
    private OAuth2Authorizer createoAuth2Authorizer(OAuth2Client oAuth2)
            throws CloudAppException {
        if (oAuth2 == null
                || !StringUtils.hasText(oAuth2.getClientId())
                || !StringUtils.hasText(oAuth2.getClientSecret())
                || !StringUtils.hasText(oAuth2.getClientSecret())
                || !StringUtils.hasText(oAuth2.getRedirectUri())
                || oAuth2.getTokenUri() == null
                || oAuth2.getScopes() == null || oAuth2.getScopes().isEmpty()) {
            throw new CloudAppException("CloudApp.InvalidAuthParameters");
        }
        
        TokenVerifier tokenVerifier = null;
        if(oAuth2.getJwksUrl() != null) {
            tokenVerifier = new JwtTokenVerifier(oAuth2.getJwksUrl());
        } else if(oAuth2.getIntrospectionUri() != null) {
            tokenVerifier =
                    new IntrospectionTokenVerifier(oAuth2.getIntrospectionUri());
        }
        
        this.authorizationService  = new AuthorizationServiceImpl(oAuth2);
        
        return new OAuth2Authorizer(authorizationService, storageService, tokenVerifier);
    }
    
    /**
     * create api key authorizer
     *
     * @return APIKeyAuthorizer
     * @throws CloudAppException if api key is invalid
     */
    private APIKeyAuthorizer createApiKeyAuthorizer(ApiKeyProperties apiKeyProperties)
            throws CloudAppException {
        
        if (apiKeyProperties == null || !StringUtils.hasText(
                apiKeyProperties.getApiKey())) {
            throw new CloudAppException("CloudApp.InvalidAuthParameters");
        }
        
        return new APIKeyAuthorizer(apiKeyProperties.getApiKey(),
                                    apiKeyProperties.getHeaderName()
        );
    }
    
    /**
     * create basic authorizer
     *
     * @return BasicAuthorizer
     * @throws CloudAppException if basic authorization configuration is invalid
     */
    private BasicAuthorizer createBasicAuthorizer(BasicProperties basicProperties)
            throws CloudAppException {
        if (basicProperties == null
                || !StringUtils.hasText(basicProperties.getUsername())
                || !StringUtils.hasText(basicProperties.getPassword())) {
            throw new CloudAppException("CloudApp.InvalidAuthParameters.Creds");
        }
        return new BasicAuthorizer(basicProperties.getUsername(),
                                   basicProperties.getPassword()
        );
    }
    
    /**
     * create jwt authorizer
     *
     * @return JWTAuthorizer
     * @throws CloudAppException if jwt authorization configuration is invalid
     */
    private JWTAuthorizer createJwtAuthorizer(JwtProperties jwtProperties)
            throws CloudAppException {
        if (jwtProperties == null
                || !StringUtils.hasText(jwtProperties.getSecret())) {
            throw new CloudAppException(
                    "CloudApp.InvalidAuthParameters.JWTSecret");
        }
        
        if (!StringUtils.hasText(jwtProperties.getAlgorithm())
                || !ApiGatewayConstant.JWT_ALGORITHM_HS256.equals(
                jwtProperties.getAlgorithm())) {
            throw new CloudAppException("CloudApp.InvalidAuthAlgorithm");
        }
        
        JWTParams params = JWTParams.builder()
                                    .base64EncodeSecret(
                                            jwtProperties.isBase64EncodeSecret())
                                    .audience(jwtProperties.getAudience())
                                    .secretKey(jwtProperties.getSecret())
                                    .algorithm(jwtProperties.getAlgorithm())
                                    .issuer(jwtProperties.getIssuer())
                                    .ttlSeconds(
                                            jwtProperties.getExpiredSecond())
                                    .subject(jwtProperties.getSubject())
                                    .build();
        
        JWTAuthorizer authorizer = new JWTAuthorizer(params);
        
        authorizer.setClaimsGenerator(request ->
                                              Collections.singletonMap("appId",
                                                                       jwtProperties.getKeyId()
                                              )
        );
        
        return authorizer;
    }
    
    @Override
    public Authorizer getAuthorizer() {
        return this.authorizer;
    }
    
    @Override
    public void afterPropertiesSet() {
        if (this.authorizer == null && properties != null) {
            this.authorizer = initAuthorizer();
        }
    }
    
    public Config getProperties() {
        return properties;
    }
    
    public void setProperties(Config properties) {
        this.properties = properties;
    }
    
    public TokenStorageService getStorageService() {
        return storageService;
    }
    
    public void setStorageService(TokenStorageService storageService) {
        this.storageService = storageService;
    }
    
    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }
    
    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }
    
}
