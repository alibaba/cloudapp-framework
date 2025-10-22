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
package com.alibaba.cloudapp.oauth2.verifier;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.alibaba.cloudapp.api.gateway.AlgorithmUtil;
import com.alibaba.cloudapp.api.oauth2.verifier.TokenVerifier;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Base64;

public class JwtTokenVerifier implements TokenVerifier {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenVerifier.class);
    
    private final JwkProvider jwkProvider;
    
    public JwtTokenVerifier(String jwksUri) {
        try {
            this.jwkProvider = new UrlJwkProvider(new URL(jwksUri));
        } catch (MalformedURLException e) {
            throw new CloudAppException(e.getMessage(), e);
        }
    }
    
    public JwtTokenVerifier(URI jwksUri) {
        try {
            this.jwkProvider = new UrlJwkProvider(jwksUri.toURL());
        } catch (MalformedURLException e) {
            throw new CloudAppException(e.getMessage(), e);
        }
    }
    
    @Override
    public boolean verify(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Jwk jwk = jwkProvider.get(jwt.getKeyId());
            
            Algorithm algorithm = AlgorithmUtil.getVerifyAlgorithm(
                    jwk.getAlgorithm(),
                    Base64.getEncoder().encode(jwk.getPublicKey().getEncoded()));
            
            algorithm.verify(jwt);
            return true;
        } catch (Exception e) {
            logger.error("Token is invalid:  {}", e.getMessage(), e);
            return false;
        }
    }
}
