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

package com.alibaba.cloudapp.oauth2.filter;

import com.alibaba.cloudapp.api.gateway.model.OAuthToken;
import com.alibaba.cloudapp.api.oauth2.AuthorizationService;
import com.alibaba.cloudapp.api.oauth2.GrantType;
import com.alibaba.cloudapp.api.oauth2.TokenStorageService;
import com.alibaba.cloudapp.api.oauth2.handler.LoginHandler;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.oauth2.OAuthConstant;
import com.alibaba.cloudapp.oauth2.handler.DefaultLoginHandler;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class OAuthCallbackFilter implements Filter, Ordered {
    
    private final AuthorizationService oauthService;
    private final TokenStorageService tokenStore;
    private LoginHandler loginHandler;
    public static final String DEFAULT_SUCCESS_URL = "/";
    
    private static final Long DEFAULT_EXPIRES_IN = LocalDateTime.MAX
            .toEpochSecond(ZoneOffset.UTC);
    
    public OAuthCallbackFilter(AuthorizationService oauthService,
                               TokenStorageService tokenStore) {
        this.oauthService = oauthService;
        this.tokenStore = tokenStore;
        this.loginHandler = new DefaultLoginHandler(DEFAULT_SUCCESS_URL);
    }
    
    public OAuthCallbackFilter(AuthorizationService oauthService,
                               TokenStorageService tokenStore,
                               LoginHandler loginHandler) {
        this.oauthService = oauthService;
        this.tokenStore = tokenStore;
        this.loginHandler = loginHandler;
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain)
            throws IOException, ServletException {
        
        if(!(servletRequest instanceof HttpServletRequest)) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        
        if (oauthService == null || !oauthService.isRedirectUrl(request.getRequestURI())
                || request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        
        try {
            OAuthToken token;
            if (oauthService.getGrantType().equals(GrantType.AUTHORIZATION_CODE)) {
                String code = request.getParameter(OAuthConstant.CODE);
                String state = request.getParameter(OAuthConstant.STATE);
                
                String codeVerifier = (String) request
                        .getSession()
                        .getAttribute(OAuthConstant.CODE_VERIFIER);
                
                Map<String, String> params = new HashMap<>();
                params.put(OAuthConstant.CODE, code);
                params.put(OAuthConstant.STATE, state);
                params.put(OAuthConstant.CODE_VERIFIER, codeVerifier);
                
                token = oauthService.getToken(oauthService.getGrantType(), params);
                
                if (token == null) {
                    throw new CloudAppException("Invalid code or state");
                }
                
            } else if (oauthService.getGrantType().equals(GrantType.IMPLICIT)) {
                
                String requestURL = request.getRequestURI();
                String accessToken = getQueryParam(
                        requestURL, OAuthConstant.ACCESS_TOKEN);
                String tokenType = getQueryParam(
                        requestURL, OAuthConstant.TOKEN_TYPE);
                String expiresIn = getQueryParam(
                        requestURL, OAuthConstant.EXPIRES_IN);
                
                if (!StringUtils.hasText(accessToken)) {
                    throw new CloudAppException("Invalid access token");
                }
                
                token = new OAuthToken();
                token.setAccessToken(accessToken);
                token.setTokenType(tokenType);
                token.setExpiresIn(expiresIn == null ? DEFAULT_EXPIRES_IN
                                           : Long.parseLong(expiresIn));
                
            } else {
                throw new CloudAppException("Invalid grant type");
            }
            
            tokenStore.saveToken(request.getSession().getId(), token);
            loginHandler.loginSuccess(request, response, token);
        } catch (Exception e) {
            loginHandler.loginFailure(request, response, e);
        }
    }
    
    private String getQueryParam(String query, String key) {
        int index = query.indexOf(key + "=");
        
        if (index < 0) {
            return null;
        }
        
        index = index + key.length() + 1;
        
        int end = query.indexOf(OAuthConstant.QUERY_SPLIT, index);
        end = end < 0 ? query.length() : end;
        
        return query.substring(index, end);
    }
    
    public LoginHandler getLoginHandler() {
        return loginHandler;
    }
    
    public void setLoginHandler(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }
    
    @Override
    public int getOrder() {
        return OAuthConstant.LOGIN_FILTER_ORDER - 1;
    }
    
}
