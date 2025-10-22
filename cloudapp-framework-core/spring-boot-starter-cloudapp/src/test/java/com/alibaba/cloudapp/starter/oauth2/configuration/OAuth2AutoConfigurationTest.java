package com.alibaba.cloudapp.starter.oauth2.configuration;

import com.alibaba.cloudapp.api.oauth2.AuthorizationService;
import com.alibaba.cloudapp.api.oauth2.TokenStorageService;
import com.alibaba.cloudapp.api.oauth2.verifier.TokenVerifier;
import com.alibaba.cloudapp.oauth2.verifier.IntrospectionTokenVerifier;
import com.alibaba.cloudapp.oauth2.verifier.JwtTokenVerifier;
import com.alibaba.cloudapp.starter.oauth2.properties.OAuth2ClientProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

class OAuth2AutoConfigurationTest {
    
    private OAuth2AutoConfiguration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new OAuth2AutoConfiguration();
    }
    
    @Test
    void testOauth2Component() throws Exception {
        // Setup
        final OAuth2ClientProperties properties = new OAuth2ClientProperties();
        properties.setJwksUrl(new URI("https://example.com/"));
        properties.setIntrospectionUri(new URI("https://example.com/"));
        properties.setEnabled(false);
        
        // Run the test
        final OAuth2Component result = configuration.oauth2Component(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testAuthorizationService() throws Exception {
        // Setup
        final OAuth2ClientProperties oAuth2ClientProperties = new OAuth2ClientProperties();
        oAuth2ClientProperties.setJwksUrl(new URI("https://example.com/"));
        oAuth2ClientProperties.setIntrospectionUri(
                new URI("https://example.com/"));
        oAuth2ClientProperties.setEnabled(false);
        final OAuth2Component component = new OAuth2Component(
                oAuth2ClientProperties);
        
        // Run the test
        final AuthorizationService result = configuration.authorizationService(
                component);
        
        // Verify the results
    }
    
    @Test
    void testStorageToken() {
        // Setup
        // Run the test
        final TokenStorageService result = configuration.storageToken();
        
        // Verify the results
    }
    
    @Test
    void testJwksTokenVerifier() throws Exception {
        // Setup
        final OAuth2ClientProperties properties = new OAuth2ClientProperties();
        properties.setJwksUrl(new URI("https://example.com/"));
        properties.setIntrospectionUri(new URI("https://example.com/"));
        properties.setEnabled(false);
        
        // Run the test
        final TokenVerifier result = configuration.tokenVerifier(properties);
        
        // Verify the results
        assert result instanceof JwtTokenVerifier;
    }
    
    @Test
    void testIntrospectionTokenVerifier() throws Exception {
        // Setup
        final OAuth2ClientProperties properties = new OAuth2ClientProperties();
        properties.setJwksUrl(new URI("https://example.com/"));
        properties.setIntrospectionUri(new URI("https://example.com/"));
        properties.setEnabled(false);
        
        // Run the test
        final TokenVerifier result = configuration.tokenVerifier(properties);
        
        // Verify the results
        assert result instanceof IntrospectionTokenVerifier;
    }
    
}
