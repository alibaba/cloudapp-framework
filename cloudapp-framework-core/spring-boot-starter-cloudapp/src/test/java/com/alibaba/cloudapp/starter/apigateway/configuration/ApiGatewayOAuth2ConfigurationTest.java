package com.alibaba.cloudapp.starter.apigateway.configuration;

import com.alibaba.cloudapp.model.OAuth2Client;
import com.alibaba.cloudapp.starter.apigateway.properties.ApiGatewayProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

class ApiGatewayOAuth2ConfigurationTest {
    
    private ApiGatewayOAuth2Configuration configuration;
    
    @BeforeEach
    void setUp() {
        configuration = new ApiGatewayOAuth2Configuration();
    }
    
    @Test
    void testAuthorizationService() {
        // Setup
        final ApiGatewayProperties properties = new ApiGatewayProperties();
        final OAuth2Client client  = new OAuth2Client();
        client.setClientId("apiKey");
        client.setClientSecret("headerName");
        client.setRedirectUri("redirect");
        properties.setOAuth2(client);
        
        final RestTemplate restTemplate = new RestTemplate(
                Collections.singletonList(new ByteArrayHttpMessageConverter()));
        
        
        // Run the test
        final OAuth2Client result = configuration.oauth2Client(
                properties);
        
        // Verify the results
        assert  result.getClientId().equals("apiKey");
    }
    
    
}
