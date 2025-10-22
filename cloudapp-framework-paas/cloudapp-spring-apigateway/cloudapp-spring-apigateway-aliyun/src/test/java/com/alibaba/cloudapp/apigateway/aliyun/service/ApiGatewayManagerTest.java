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

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.alibaba.cloudapp.apigateway.aliyun.properties.ApiKeyProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.BasicProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.JwtProperties;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiGatewayManagerTest {
    
    @Mock
    IAcsClient client;
    @Mock
    CommonResponse res;
    private ApiGatewayManager gatewayManager;
    
    @Before
    public void setUp() throws Exception {
        gatewayManager = new ApiGatewayManager(
                "accessKey",
                "secretKey",
                "gatewayUri",
                "regionId",
                "resourceGroupId",
                "organizationId"
                ,false
        );
        Field f =  ApiGatewayManager.class.getDeclaredField("client");
        f.setAccessible(true);
        f.set(gatewayManager, client);
        
        doReturn(res).when(client).getCommonResponse(any(CommonRequest.class));
        HttpResponse httpResponse = new HttpResponse("url");
        httpResponse.setStatus(200);
        
        doReturn(httpResponse).when(res).getHttpResponse();
        
    }
    
    @Test
    public void testCreateApiKeyConsumer() {
        // Setup
        final ApiKeyProperties apiKey = new ApiKeyProperties();
        apiKey.setApiKey("apiKey");
        apiKey.setHeaderName("headerName");
        
        // Run the test
        gatewayManager.createApiKeyConsumer(
                "name", "gwInstanceId",
                Collections.singletonList("value"),
                apiKey
        );
        
        // Verify the results
    }
    
    @Test
    public void testCreateApiKeyConsumer_RestTemplateThrowsRestClientException()
            throws ClientException {
        // Setup
        final ApiKeyProperties apiKey = new ApiKeyProperties();
        apiKey.setApiKey("apiKey");
        apiKey.setHeaderName("headerName");
        
        when(client.getCommonResponse(any(CommonRequest.class)))
                .thenThrow(RestClientException.class);
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> gatewayManager.createApiKeyConsumer(
                             "name", "gwInstanceId",
                             Collections.singletonList("value"),
                             apiKey
                     )
        );
    }
    
    @Test
    public void testCreateJwtConsumer() {
        // Setup
        final JwtProperties jwt = new JwtProperties();
        jwt.setKeyId("keyId");
        jwt.setSecret("secret");
        jwt.setIssuer("issuer");
        jwt.setSubject("subject");
        jwt.setExpiredSecond(0L);
        
        // Run the test
        gatewayManager.createJwtConsumer(
                "name", "gwInstanceId",
                Collections.singletonList("value"), jwt
        );
        
        // Verify the results
    }
    
    @Test
    public void testCreateJwtConsumer_RestTemplateThrowsRestClientException()
            throws ClientException {
        // Setup
        final JwtProperties jwt = new JwtProperties();
        jwt.setKeyId("keyId");
        jwt.setSecret("secret");
        jwt.setIssuer("issuer");
        jwt.setSubject("subject");
        jwt.setExpiredSecond(0L);
        
        when(client.getCommonResponse(any(CommonRequest.class)))
                .thenThrow(RestClientException.class);
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> gatewayManager.createJwtConsumer(
                             "name", "gwInstanceId",
                             Collections.singletonList("value"), jwt
                     )
        );
    }
    
    @Test
    public void testCreateBasicConsumer() {
        // Setup
        final BasicProperties basic = new BasicProperties();
        basic.setUsername("username");
        basic.setPassword("password");
        
        
        // Run the test
        gatewayManager.createBasicConsumer(
                "name", "gwInstanceId",
                Collections.singletonList("value"), basic
        );
        
        // Verify the results
    }
    
    @Test
    public void testCreateBasicConsumer_RestTemplateThrowsRestClientException()
            throws ClientException {
        // Setup
        final BasicProperties basic = new BasicProperties();
        basic.setUsername("username");
        basic.setPassword("password");
        
        when(client.getCommonResponse(any(CommonRequest.class)))
                .thenThrow(RestClientException.class);
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> gatewayManager.createBasicConsumer(
                             "name", "gwInstanceId",
                             Collections.singletonList("value"), basic
                     )
        );
    }
    
    @Test
    public void testCheckConsumerExists() {
        // Setup
        
        // Run the test
        when(res.getData()).thenReturn("{\"data\": {\"records\": [{\"appName\":\"name\"}]}}");
        
        
        final boolean result = gatewayManager.checkConsumerExists(
                "name", "gwInstanceId");
        
        // Verify the results
        assertTrue(result);
    }
    
    @Test
    public void testCheckConsumerExists_RestTemplateThrowsRestClientException()
            throws ClientException {
        // Setup
        when(client.getCommonResponse(any(CommonRequest.class)))
                .thenThrow(RestClientException.class);
        
        // Run the test
        assertThrows(CloudAppException.class,
                     () -> gatewayManager.checkConsumerExists(
                             "name", "gwInstanceId")
        );
    }
    
    @Test
    public void testInitRequestEntity() throws Exception {
        // Setup
        final Map<String, Object> query = new HashMap<>();
        final MediaType type = new MediaType("type", "subtype",
                                             StandardCharsets.UTF_8
        );
        
        when(res.getData()).thenReturn("query");
        
        // Run the test
        final CommonResponse result = gatewayManager.requestServer(
                "path", MethodType.GET, query
        );
        // Verify the results
        assertEquals(result.getData(), "query");
    }
    
    @Test
    public void testInitRequestEntity_ThrowsCloudAppInvalidAccessException() {
        // Setup
        final Map<String, String> query = new HashMap<>();
        final Map<String, Object> body = new HashMap<>();
        final MediaType type = new MediaType(
                "type", "subtype", StandardCharsets.UTF_8
        );
        
        gatewayManager = new ApiGatewayManager(null,
                                               null,
                                               "gatewayUri",
                                               null,
                                               null,
                                               null,
                                               false
        );
        
        // Run the test
        assertThrows(NullPointerException.class,
                     () -> gatewayManager.requestServer("path", MethodType.GET, body)
        );
    }
    
}
