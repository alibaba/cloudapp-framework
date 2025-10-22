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

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.cloudapp.api.gateway.authentication.Authorizer;
import com.alibaba.cloudapp.api.gateway.authentication.BasicAuthorizer;
import com.alibaba.cloudapp.api.gateway.authentication.NoAuthorization;
import com.alibaba.cloudapp.api.oauth2.AuthorizationService;
import com.alibaba.cloudapp.api.oauth2.TokenStorageService;
import com.alibaba.cloudapp.apigateway.aliyun.ApiGatewayConstant;
import com.alibaba.cloudapp.apigateway.aliyun.AuthTypeEnum;
import com.alibaba.cloudapp.apigateway.aliyun.properties.ApiKeyProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.BasicProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.Config;
import com.alibaba.cloudapp.apigateway.aliyun.properties.JwtProperties;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.model.OAuth2Client;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizedServiceTest {
    
    @Mock
    private Authorizer mockAuthorizer;
    
    @Mock
    private TokenStorageService storageService;
    
    @Mock
    private AuthorizationService authorizationService;
    
    
    @InjectMocks
    private AuthorizedService authorizedServiceUnderTest;
    
    @Before
    public void setUp() throws Exception {
//        authorizedServiceUnderTest = new AuthorizedService(mockAuthorizer);
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testInitAuthorizer_APIKEY() {
        Config properties = mock(Config.class);
        when(properties.getAuthType()).thenReturn(AuthTypeEnum.APIKEY);
        ApiKeyProperties apiKeyProperties = mock(ApiKeyProperties.class);
        when(apiKeyProperties.getApiKey()).thenReturn("key");
        when(properties.getApiKey()).thenReturn(apiKeyProperties);
        
        authorizedServiceUnderTest = new AuthorizedService(properties);
        authorizedServiceUnderTest.initAuthorizer();
        
    }
    
    @Test
    public void testInitAuthorizer_BASIC() {
        Config properties = mock(Config.class);
        when(properties.getAuthType()).thenReturn(AuthTypeEnum.BASIC);
        BasicProperties basicProperties = mock(BasicProperties.class);
        when(basicProperties.getUsername()).thenReturn("username");
        when(basicProperties.getPassword()).thenReturn("password");
        when(properties.getBasic()).thenReturn(basicProperties);
        
        authorizedServiceUnderTest = new AuthorizedService(properties);
        authorizedServiceUnderTest.initAuthorizer();
    }
    
    @Test
    public void testInitAuthorizer_NONE() {
        Config properties = mock(Config.class);
        when(properties.getAuthType()).thenReturn(AuthTypeEnum.NO_AUTH);
        authorizedServiceUnderTest = new AuthorizedService(properties);
        Authorizer authorizer = authorizedServiceUnderTest.initAuthorizer();
        assertTrue(authorizer instanceof NoAuthorization);
    }
    
    @Test
    public void testInitAuthorizer_JWT() {
        Config properties = mock(Config.class);
        when(properties.getAuthType()).thenReturn(AuthTypeEnum.JWT);
        JwtProperties jwtProperties = mock(JwtProperties.class);
        when(jwtProperties.getAlgorithm()).thenReturn(ApiGatewayConstant.JWT_ALGORITHM_HS256);
        when(jwtProperties.getSecret()).thenReturn("secret");
        when(jwtProperties.getIssuer()).thenReturn("iss");
        when(jwtProperties.getAudience()).thenReturn("aud");
        when(jwtProperties.getSubject()).thenReturn("sub");
//        when(jwtProperties.getKeyId()).thenReturn("key");
        when(properties.getJwt()).thenReturn(jwtProperties);
        
        authorizedServiceUnderTest = new AuthorizedService(properties);
        authorizedServiceUnderTest.initAuthorizer();
    }
    
    @Test
    public void testInitAuthorizer_OAUTH2() {
        Config properties = mock(Config.class);
        when(properties.getAuthType()).thenReturn(AuthTypeEnum.OAUTH2);
        OAuth2Client client = mock(OAuth2Client.class);
        when(client.getClientId()).thenReturn("clientId");
        when(client.getClientSecret()).thenReturn("clientSecret");
        when(client.getRedirectUri()).thenReturn("redirectUri");
        when(client.getTokenUri()).thenReturn(URI.create("tokenUri"));
//        when(client.getAuthorizationUri()).thenReturn(URI.create("authorizationUri"));
        when(client.getScopes()).thenReturn(Collections.singletonList("scope"));
//        when(client.getGrantTypes()).thenReturn(Collections.singletonList("grantType"));
//        when(client.isEnablePkce()).thenReturn(true);
        when(properties.getOAuth2()).thenReturn(client);
        
        authorizedServiceUnderTest = new AuthorizedService(properties);
        authorizedServiceUnderTest.initAuthorizer();
    }
   
    
    @Test
    public void testInitAuthorizer_ThrowsCloudAppException() {
        // Setup
        // Run the test
        assertThrows(IllegalArgumentException.class,
                     () -> authorizedServiceUnderTest.initAuthorizer()
        );
    }
    
    @Test
    public void testGetAuthorizer() {
        assertEquals(mockAuthorizer,
                     authorizedServiceUnderTest.getAuthorizer()
        );
    }
    
    @Test
    public void testAfterPropertiesSet() {
        // Setup
        // Run the test
        authorizedServiceUnderTest.afterPropertiesSet();
        
        // Verify the results
    }
    
    @Test
    public void testAfterPropertiesSet_ThrowsException() {
        // Setup
        Config properties = mock(Config.class);
        when(properties.getAuthType()).thenReturn(AuthTypeEnum.APIKEY);
        
        authorizedServiceUnderTest = new AuthorizedService(properties);
        // Run the test
        assertThrows(Exception.class,
                     () -> authorizedServiceUnderTest.afterPropertiesSet()
        );
    }
    
    
    private void createBasicAuthorizer(BasicProperties properties)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method createBasicAuthorizerMethod = AuthorizedService.class.getDeclaredMethod(
                "createBasicAuthorizer", BasicProperties.class);
        createBasicAuthorizerMethod.setAccessible(true);
        createBasicAuthorizerMethod.invoke(authorizedServiceUnderTest, properties);
    }
    
    private void createoAuth2Authorizer(OAuth2Client client)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method createoAuth2AuthorizerMethod = AuthorizedService.class.getDeclaredMethod(
                "createoAuth2Authorizer", OAuth2Client.class);
        createoAuth2AuthorizerMethod.setAccessible(true);
        createoAuth2AuthorizerMethod.invoke(authorizedServiceUnderTest, client);
    }
    
    
    @Test(expected = Exception.class)
    public void createBasicAuthorizer_NullBasicProperties_ThrowsCloudAppException()
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        createBasicAuthorizer(null);
    }
    
    @Test(expected = Exception.class)
    public void createBasicAuthorizer_EmptyUsername_ThrowsCloudAppException()
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        BasicProperties basicProperties = new BasicProperties();
        basicProperties.setUsername("");
        basicProperties.setPassword("password");
        
//        assertThrows(CloudAppException.class, () -> authorizedServiceUnderTest.createBasicAuthorizer(basicProperties));
        createBasicAuthorizer(basicProperties);
    }
    
    @Test(expected = Exception.class)
    public void createBasicAuthorizer_EmptyPassword_ThrowsCloudAppException()
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        BasicProperties basicProperties = new BasicProperties();
        basicProperties.setUsername("username");
        basicProperties.setPassword("");
    
        createBasicAuthorizer(basicProperties);
    }
    
    @Test
    public void createBasicAuthorizer_ValidBasicProperties_ReturnsBasicAuthorizer()
            throws CloudAppException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        BasicProperties basicProperties = new BasicProperties();
        basicProperties.setUsername("username");
        basicProperties.setPassword("password");
    
        Method createBasicAuthorizerMethod = AuthorizedService.class.getDeclaredMethod(
                "createBasicAuthorizer", BasicProperties.class);
        createBasicAuthorizerMethod.setAccessible(true);
        BasicAuthorizer invoke = (BasicAuthorizer) createBasicAuthorizerMethod.invoke(
                authorizedServiceUnderTest, basicProperties);
    }
    
    
    
    @Test(expected = Exception.class)
    public void createoAuth2Authorizer_NullOAuth2Client_ThrowsCloudAppException()
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        createoAuth2Authorizer(null);
    }
    
    @Test(expected = Exception.class)
    public void createoAuth2Authorizer_MissingClientId_ThrowsCloudAppException()
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        OAuth2Client oAuth2Client = new OAuth2Client();
        oAuth2Client.setClientSecret("secret");
        oAuth2Client.setRedirectUri("http://redirect");
        oAuth2Client.setTokenUri(URI.create("http://token"));
        oAuth2Client.setScopes(Collections.singletonList("scope"));
        
        createoAuth2Authorizer(oAuth2Client);
    }
    
    @Test(expected = Exception.class)
    public void createoAuth2Authorizer_MissingClientSecret_ThrowsCloudAppException()
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        OAuth2Client oAuth2Client = new OAuth2Client();
        oAuth2Client.setClientId("id");
        oAuth2Client.setRedirectUri("http://redirect");
        oAuth2Client.setTokenUri(URI.create("http://token"));
        oAuth2Client.setScopes(Collections.singletonList("scope"));
        
        createoAuth2Authorizer(oAuth2Client);
    }
    
    @Test(expected = Exception.class)
    public void createoAuth2Authorizer_MissingRedirectUri_ThrowsCloudAppException()
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        OAuth2Client oAuth2Client = new OAuth2Client();
        oAuth2Client.setClientId("id");
        oAuth2Client.setClientSecret("secret");
        oAuth2Client.setTokenUri(URI.create("http://token"));
        oAuth2Client.setScopes(Collections.singletonList("scope"));
        
        createoAuth2Authorizer(oAuth2Client);
    }
    
    @Test(expected = Exception.class)
    public void createoAuth2Authorizer_MissingTokenUri_ThrowsCloudAppException()
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        OAuth2Client oAuth2Client = new OAuth2Client();
        oAuth2Client.setClientId("id");
        oAuth2Client.setClientSecret("secret");
        oAuth2Client.setRedirectUri("http://redirect");
        oAuth2Client.setScopes(Collections.singletonList("scope"));
        
        createoAuth2Authorizer(oAuth2Client);
    }
    
    @Test(expected = Exception.class)
    public void createoAuth2Authorizer_MissingScopes_ThrowsCloudAppException()
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        OAuth2Client oAuth2Client = new OAuth2Client();
        oAuth2Client.setClientId("id");
        oAuth2Client.setClientSecret("secret");
        oAuth2Client.setRedirectUri("http://redirect");
        oAuth2Client.setTokenUri(URI.create("http://token"));
        
        createoAuth2Authorizer(oAuth2Client);
    }
    
    @Test
    public void createoAuth2Authorizer_ValidOAuth2ClientWithJwksUrl_ReturnsOAuth2Authorizer()
            throws CloudAppException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        OAuth2Client oAuth2Client = new OAuth2Client();
        oAuth2Client.setClientId("id");
        oAuth2Client.setClientSecret("secret");
        oAuth2Client.setRedirectUri("http://redirect");
        oAuth2Client.setTokenUri(URI.create("http://token"));
        oAuth2Client.setScopes(Collections.singletonList("scope"));
        oAuth2Client.setJwksUrl(URI.create("http://jwks"));
     
        createoAuth2Authorizer(oAuth2Client);
    }
    
    @Test
    public void createoAuth2Authorizer_ValidOAuth2ClientWithIntrospectionUri_ReturnsOAuth2Authorizer()
            throws CloudAppException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        OAuth2Client oAuth2Client = new OAuth2Client();
        oAuth2Client.setClientId("id");
        oAuth2Client.setClientSecret("secret");
        oAuth2Client.setRedirectUri("http://redirect");
        oAuth2Client.setTokenUri(URI.create("http://token"));
        oAuth2Client.setScopes(Collections.singletonList("scope"));
        oAuth2Client.setIntrospectionUri(URI.create("http://introspect"));
        
        createoAuth2Authorizer(oAuth2Client);
    }
    
    
    
}
