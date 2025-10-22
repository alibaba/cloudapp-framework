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
package com.alibaba.cloudapp.model;

import java.net.URI;
import java.util.List;

public class OAuth2Client {
    private String clientId;
    
    private String clientSecret;
    
    private List<String> scopes;
    
    private String redirectUri;
    
    private URI tokenUri;
    
    private List<String> grantTypes;
    
    private URI authorizationUri;
    
    private URI jwksUrl;
    
    private URI introspectionUri;
    
    private List<String> filterSkipUrls;
    
    private String loginSuccessUrl;
    
    private boolean enablePkce = false;
    
    public List<String> getFilterSkipUrls() {
        return filterSkipUrls;
    }
    
    public void setFilterSkipUrls(List<String> filterSkipUrls) {
        this.filterSkipUrls = filterSkipUrls;
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public String getClientSecret() {
        return clientSecret;
    }
    
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
    
    public List<String> getScopes() {
        return scopes;
    }
    
    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }
    
    public String getRedirectUri() {
        return redirectUri;
    }
    
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
    
    public URI getTokenUri() {
        return tokenUri;
    }
    
    public void setTokenUri(URI tokenUri) {
        this.tokenUri = tokenUri;
    }
    
    public List<String> getGrantTypes() {
        return grantTypes;
    }
    
    public void setGrantTypes(List<String> grantTypes) {
        this.grantTypes = grantTypes;
    }
    
    public URI getAuthorizationUri() {
        return authorizationUri;
    }
    
    public void setAuthorizationUri(URI authorizationUri) {
        this.authorizationUri = authorizationUri;
    }
    
    public URI getJwksUrl() {
        return jwksUrl;
    }
    
    public void setJwksUrl(URI jwksUrl) {
        this.jwksUrl = jwksUrl;
    }
    
    public URI getIntrospectionUri() {
        return introspectionUri;
    }
    
    public void setIntrospectionUri(URI introspectionUri) {
        this.introspectionUri = introspectionUri;
    }
    
    public boolean isEnablePkce() {
        return enablePkce;
    }
    
    public void setEnablePkce(boolean enablePkce) {
        this.enablePkce = enablePkce;
    }
    
    public String getLoginSuccessUrl() {
        return loginSuccessUrl;
    }
    
    public void setLoginSuccessUrl(String loginSuccessUrl) {
        this.loginSuccessUrl = loginSuccessUrl;
    }
}
