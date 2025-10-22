package com.alibaba.cloudapp.starter.apigateway.configuration;

import com.alibaba.cloudapp.apigateway.aliyun.service.ApiGatewayManager;
import com.alibaba.cloudapp.starter.apigateway.properties.ApiGatewayManagerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApiGatewayManagerRefreshComponentTest {
    
    @Mock
    private ApiGatewayManagerProperties mockProperties;
    
    private ApiGatewayManagerRefreshComponent apiGatewayManagerRefreshComponentUnderTest;
    
    @BeforeEach
    void setUp() {
        apiGatewayManagerRefreshComponentUnderTest = new ApiGatewayManagerRefreshComponent(
                mockProperties);
    }
    
    @Test
    void testPostStart() {
        // Setup
        when(mockProperties.getAccessKey()).thenReturn("accessKey");
        when(mockProperties.getSecretKey()).thenReturn("secretKey");
        when(mockProperties.getGatewayUri()).thenReturn("gatewayUri");
        
        // Run the test
        apiGatewayManagerRefreshComponentUnderTest.postStart();
        
        // Verify the results
    }
    
    @Test
    void testPreStop() {
        apiGatewayManagerRefreshComponentUnderTest.preStop();
    }
    
    @Test
    void testBindKey() {
        assertEquals("io.cloudapp.apigateway.aliyun.server",
                     apiGatewayManagerRefreshComponentUnderTest.bindKey()
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("apiGateway.manager",
                     apiGatewayManagerRefreshComponentUnderTest.getName()
        );
    }
    
    @Test
    void testCreateBean() {
        // Setup
        final ApiGatewayManagerProperties properties = new ApiGatewayManagerProperties();
        properties.setAccessKey("accessKey");
        properties.setSecretKey("secretKey");
        properties.setGatewayUri("gatewayUri");
        
        // Run the test
        final ApiGatewayManager result = apiGatewayManagerRefreshComponentUnderTest.createBean(
                properties);
        
        // Verify the results
    }
    
}
