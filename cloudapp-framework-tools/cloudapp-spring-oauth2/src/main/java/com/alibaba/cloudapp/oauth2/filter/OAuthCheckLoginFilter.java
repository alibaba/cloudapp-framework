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
import com.alibaba.cloudapp.api.oauth2.verifier.TokenVerifier;
import com.alibaba.cloudapp.oauth2.OAuthConstant;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OAuthCheckLoginFilter implements Filter, Ordered {
    
    private final TokenStorageService tokenStore;
    private final TokenVerifier tokenVerifier;
    private final AuthorizationService authorizationService;
    
    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();
    
    private final List<String> skipPaths = new ArrayList<>();
    
    public OAuthCheckLoginFilter(TokenStorageService tokenStore,
                                 TokenVerifier tokenVerifier,
                                 AuthorizationService authorizationService) {
        this.tokenStore = tokenStore;
        this.tokenVerifier = tokenVerifier;
        this.authorizationService = authorizationService;
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        
        if (!(servletRequest instanceof HttpServletRequest)
                || isSkipUrl(((HttpServletRequest) servletRequest).getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String verifierToken = getToken(request);
        String refreshToken = getRefreshToken(request);
        
        //refreshToken
        if (StringUtils.hasText(verifierToken) && StringUtils.hasText(refreshToken)
                && !tokenVerifier.verify(verifierToken)) {
            
            Map<String, String> params = Collections.singletonMap(
                    OAuthConstant.REFRESH_TOKEN, refreshToken
            );
            
            OAuthToken token = authorizationService.getToken(
                    GrantType.REFRESH_TOKEN, params);
            
            tokenStore.saveToken(request.getSession().getId(), token);
        }
        
        if (!StringUtils.hasText(verifierToken)) {
            reLogin(request, (HttpServletResponse) servletResponse);
            return;
        }
        
        filterChain.doFilter(servletRequest, servletResponse);
    }
    
    private void reLogin(HttpServletRequest request,
                         HttpServletResponse response) {
        Map<String, String> urlMap = authorizationService
                .getCodeUrl(request.getRequestURI());
        
        if (urlMap.containsKey(OAuthConstant.CODE_VERIFIER)) {
            request.getSession().setAttribute(
                    OAuthConstant.CODE_VERIFIER,
                    urlMap.get(OAuthConstant.CODE_VERIFIER)
            );
        }
        
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader(HttpHeaders.LOCATION,
                           urlMap.get(OAuthConstant.AUTHORIZATION_URI)
        );
    }
    
    public List<String> getSkipPaths() {
        return skipPaths;
    }
    
    public void setSkipPaths(List<String> skipPaths) {
        if (skipPaths != null && !skipPaths.isEmpty()) {
            this.skipPaths.addAll(
                    skipPaths.stream().filter(StringUtils::hasText)
                             .collect(Collectors.toList())
            );
        }
    }
    
    public void addSkipUrls(String url) {
        if (!this.skipPaths.contains(url) && StringUtils.hasText(url)) {
            this.skipPaths.add(url);
        }
    }
    
    public boolean isSkipUrl(String url) {
        return this.skipPaths.stream().anyMatch(
                e -> PATH_MATCHER.match(e, url));
    }
    
    public String getToken(HttpServletRequest request) {
        OAuthToken token = tokenStore.getToken(request.getSession().getId());
        return token == null ? null : StringUtils.hasText(token.getIdToken())
                ? token.getIdToken() : token.getAccessToken();
    }
    
    public String getRefreshToken(HttpServletRequest request) {
        OAuthToken token = tokenStore.getToken(request.getSession().getId());
        return token  == null ? null : token.getRefreshToken();
    }
    
    @Override
    public int getOrder() {
        return OAuthConstant.LOGIN_FILTER_ORDER;
    }
    
}
