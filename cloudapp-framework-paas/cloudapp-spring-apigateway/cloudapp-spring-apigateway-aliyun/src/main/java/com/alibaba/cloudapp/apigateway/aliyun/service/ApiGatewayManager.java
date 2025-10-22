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

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpClientConfig;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.alibaba.cloudapp.apigateway.aliyun.ApiGatewayConstant;
import com.alibaba.cloudapp.apigateway.aliyun.AuthTypeEnum;
import com.alibaba.cloudapp.apigateway.aliyun.properties.ApiKeyProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.BasicProperties;
import com.alibaba.cloudapp.apigateway.aliyun.properties.JwtProperties;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSB APIGateway
 */
public class ApiGatewayManager {
    
    private static final Logger logger = LoggerFactory.getLogger(
            ApiGatewayManager.class);
    
    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String organizationId;
    private String resourceGroupId;
    private String productCode = "csb2.0";
    private String sdkSource = "apiGateway";
    private String regionId;
    private boolean ssl;
    private IAcsClient client;
    public ApiGatewayManager(
            String accessKey, String secretKey, String gatewayUri,
            String regionId, String resourceGroupId, String organizationId,
            boolean ssl
    ) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.endpoint = getEndpoint(gatewayUri);
        this.organizationId = organizationId;
        this.resourceGroupId = resourceGroupId;
        this.regionId = regionId;
        this.ssl = ssl;
        
        initClient();
    }
    
    private void initClient() {
        DefaultProfile profile = DefaultProfile.getProfile(
                regionId, accessKey, secretKey);
        HttpClientConfig clientConfig = HttpClientConfig.getDefault();
        clientConfig.setIgnoreSSLCerts(ssl);
        clientConfig.setConnectionTimeoutMillis(3000L);
        clientConfig.setReadTimeoutMillis(10000L);
        profile.setHttpClientConfig(clientConfig);
        client = new DefaultAcsClient(profile);
    }
    
    public void createApiKeyConsumer(
            String name,
            String gwInstanceId,
            List<String> groups,
            ApiKeyProperties apiKey
    ) throws CloudAppException {
        JSONObject body = new JSONObject();
        
        body.put("appName", name);
        body.put("authType", AuthTypeEnum.APIKEY.getType());
        body.put("gwInstanceId", gwInstanceId);
        body.put("groups", groups == null ? Collections.emptyList() : groups);
        body.put("key", apiKey.getApiKey());
        
        createConsumer(body);
    }
    
    public void createJwtConsumer(
            String name,
            String gwInstanceId,
            List<String> groups,
            JwtProperties jwt
    ) throws CloudAppException {
        JSONObject body = new JSONObject();
        body.put("appName", name);
        body.put("authType", AuthTypeEnum.JWT.getType());
        body.put("gwInstanceId", gwInstanceId);
        body.put("groups", groups == null ? Collections.emptyList() : groups);
        
        body.put("key", jwt.getKeyId());
        body.put("appSecret", jwt.getSecret());
        body.put("expireTime", jwt.getExpiredSecond() * 1000);
        Map<String, String> payload = new HashMap<>(4);
        
        payload.put("issuer", jwt.getIssuer());
        payload.put("subject", jwt.getSubject());
        
        body.put("payload", payload);
        createConsumer(body);
    }
    
    public void createBasicConsumer(
            String name,
            String gwInstanceId,
            List<String> groups,
            BasicProperties basic
    ) throws CloudAppException {
        JSONObject body = new JSONObject();
        body.put("appName", name);
        body.put("authType", AuthTypeEnum.BASIC.getType());
        body.put("gwInstanceId", gwInstanceId);
        body.put("groups", groups == null ? Collections.emptyList() : groups);
        
        body.put("key", basic.getUsername());
        body.put("password", basic.getPassword());
        
        createConsumer(body);
    }
    
    private void createConsumer(JSONObject body) throws CloudAppException {
        try {
            CommonResponse response = requestServer(
                    ApiGatewayConstant.CREATE_CONSUMER_URL,
                    MethodType.POST,
                    body
            );
            
            if (response.getHttpResponse().isSuccess()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("create consumer response: {}",
                                 response.getData()
                    );
                }
            } else {
                throw new CloudAppException("CloudApp.CreateConsumerFailed");
            }
        } catch (Exception e) {
            throw new CloudAppException("create consumer failed", e);
        }
    }
    
    /**
     * Check consumer exists
     *
     * @param name         api gateway name
     * @param gwInstanceId api gateway instance id
     * @return true if app exists, false otherwise
     * @throws CloudAppException if check app exists failed
     */
    public boolean checkConsumerExists(String name, String gwInstanceId)
            throws CloudAppException {
        try {
            Map<String, Object> params = new HashMap<>(8);
            params.put("appName", name);
            params.put("gwInstanceId", gwInstanceId);
            params.put("activeSearchName", "appName");
            params.put("current", 1);
            params.put("size", 20);
            
            CommonResponse response = requestServer(
                    ApiGatewayConstant.LIST_CONSUMER_URL,
                    MethodType.POST,
                    params
            );
            
            if (response.getHttpResponse().isSuccess()) {
                
                if (logger.isDebugEnabled()) {
                    logger.debug("list consumer response: {}",
                                 response.getData()
                    );
                }
                
                JSONObject result = JSON.parseObject(response.getData());
                JSONArray dataList = result == null || result.get(
                        "data") == null ?
                        new JSONArray() : result.getJSONObject("data")
                                                .getJSONArray("records");
                if (dataList != null && !dataList.isEmpty()) {
                    
                    return dataList.stream().anyMatch(e -> {
                        JSONObject app = (JSONObject) JSON.toJSON(e);
                        return app.getString("appName").equals(name);
                    });
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("list consumer response status code: {}",
                                 response.getHttpStatus()
                    );
                }
            }
            return false;
        } catch (Exception e) {
            throw new CloudAppException("check app exists failed",
                                        "CloudApp.AppAlreadyExisted", e
            );
        }
    }
    
    public CommonResponse requestServer(
            String path, MethodType method, Map<String, Object> params
    ) throws ClientException {
        CommonRequest request = new CommonRequest();
        request.setSysUriPattern(path);
        request.setHttpContent(
                JSONObject.toJSONString(params).getBytes(StandardCharsets.UTF_8),
                "UTF-8", FormatType.JSON
        );
        request.setSysDomain(endpoint);
        request.setSysProduct(productCode);
        request.setSysVersion("2023-02-06");
        request.putHeadParameter("x-acs-caller-sdk-source", sdkSource);
        request.putHeadParameter("x-acs-resourcegroupid", resourceGroupId);
        request.putHeadParameter("x-acs-organizationid", organizationId);
//       request.putHeadParameter("x-acs-caller-type", "custo");
//      request.putHeaderParameter("x-acs-roleId", settingInfo.getResourceGroupId());
        
        request.setSysMethod(method);
        return client.getCommonResponse(request);
    }
    
    private String getEndpoint(String gatewayUrl) {
        if(!StringUtils.hasText(gatewayUrl)) {
            throw new IllegalArgumentException("gatewayUri is empty");
        }
        if(gatewayUrl.startsWith("http")) {
            gatewayUrl =  endpoint.replaceFirst("https?://", "");
        }
        if(gatewayUrl.contains("/")) {
            gatewayUrl = gatewayUrl.split("/")[0];
        }
        return gatewayUrl;
    }
    
    public void refresh() {
        initClient();
    }
    
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
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public void setEndpoint(String gatewayUri) {
        this.endpoint = getEndpoint(gatewayUri);
    }
    
    public String getOrganizationId() {
        return organizationId;
    }
    
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    
    public String getResourceGroupId() {
        return resourceGroupId;
    }
    
    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
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
    
    public boolean isSsl() {
        return ssl;
    }
    
    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }
    
    public String getRegionId() {
        return regionId;
    }
    
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
