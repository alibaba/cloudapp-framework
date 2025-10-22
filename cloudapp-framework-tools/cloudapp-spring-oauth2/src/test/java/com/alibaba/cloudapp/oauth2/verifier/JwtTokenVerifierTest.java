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

import com.alibaba.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

/**
 * token must have the jwt header kid
 */
public class JwtTokenVerifierTest {
    
    private JwtTokenVerifier jwtTokenVerifierUnderTest;
    
    @Before
    public void setUp() throws Exception {
        jwtTokenVerifierUnderTest = new JwtTokenVerifier(
                "https://gitlab.com/oauth/discovery/keys");
    }
    
    @Test
    public void testVerify() {
        // Setup
        // Run the test
        final boolean result = jwtTokenVerifierUnderTest.verify(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        );
        
        // Verify the results
        assertFalse(result);
    }
    
    @Test
    public void JwtTokenVerifier_ValidUri_ShouldInitializeSuccessfully() throws Exception {
        URI validUri = new URI("https://gitlab.com/oauth/discovery/keys");
        jwtTokenVerifierUnderTest = new JwtTokenVerifier(validUri);
        assertNotNull(jwtTokenVerifierUnderTest);
    }
    
    @Test
    public void JwtTokenVerifier_InvalidUri_ShouldThrowCloudAppException()
            throws URISyntaxException {
        URI invalidUri = new URI("invalid_uri");
        assertThrows(Exception.class, () -> new JwtTokenVerifier(invalidUri));
    }
    
}