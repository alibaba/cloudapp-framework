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

package com.alibaba.cloudapp.apigateway.proxy.demo.controller;

import com.alibaba.cloudapp.api.gateway.GatewayProxyServletFilter;
import com.alibaba.cloudapp.api.gateway.authentication.APIKeyAuthorizer;
import com.alibaba.cloudapp.api.gateway.authentication.Authorizer;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;

@WebFilter(filterName = "ProxyGatewayDemoFilter", urlPatterns = "/proxy")
public class ProxyGatewayDemoFilter extends GatewayProxyServletFilter {

    @Value("${io.cloudapp.apigateway.aliyun.brokerUrl:}")
    private String brokerUrl;

    @Value("${apiKey.apiKey:}")
    private String apiKey;

    @Value("${apiKey.headerName:}")
    private String apiKeyHeader;

    @Override
    public void proxy(ServletRequest request, ServletResponse response) throws CloudAppException {
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper((HttpServletRequest) request);
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        URL targetUrl = null;
        try {
            targetUrl = new URL(brokerUrl + "/c/consumer/alive");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity;
        try {
            restTemplate.setInterceptors(Collections.singletonList((request1, body, execution) -> {
                try {
                    getAuthorizer().applyAuthorization(request1);
                } catch (CloudAppException e) {
                    e.printStackTrace();
                }

                return execution.execute(request1, body);
            }));
            responseEntity = restTemplate.exchange(
                    targetUrl.toURI(),
                    HttpMethod.valueOf(httpRequest.getMethod()),
                    createHttpEntity(httpRequest),
                    String.class
            );
        } catch (URISyntaxException e) {
            throw new CloudAppException("Invalid URI", e);
        }
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(responseEntity.getStatusCodeValue());
        httpResponse.setContentType(responseEntity.getHeaders().getContentType().toString());
        try (PrintWriter writer = httpResponse.getWriter()) {
            writer.print(responseEntity.getBody());
        } catch (IOException e) {
            throw new CloudAppException("Error writing response", e);
        }
    }

    private HttpEntity<?> createHttpEntity(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headerValues = request.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                String headerValue = headerValues.nextElement();
                headers.add(headerName, headerValue);
            }
        }
        byte[] body = readRequestBody(request);
        return new HttpEntity<>(body, headers);
    }

    private byte[] readRequestBody(HttpServletRequest request) {
        try (InputStream input = request.getInputStream();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                output.write(buffer, 0, len);
            }
            return output.toByteArray();
        } catch (IOException e) {
            try {
                throw new CloudAppException("Error reading request body", e);
            } catch (CloudAppException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public Authorizer getAuthorizer() throws CloudAppException {
        APIKeyAuthorizer authorizer = new APIKeyAuthorizer(apiKey, apiKeyHeader);
        return authorizer;
    }
}
