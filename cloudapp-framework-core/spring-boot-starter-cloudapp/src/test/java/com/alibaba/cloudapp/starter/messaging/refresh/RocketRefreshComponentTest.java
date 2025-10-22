package com.alibaba.cloudapp.starter.messaging.refresh;

import com.alibaba.cloudapp.starter.messaging.factory.CloudAppRocketBeanFactory;
import com.alibaba.cloudapp.starter.messaging.properties.CloudAppRocketProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RocketRefreshComponentTest {
    
    @Mock
    private CloudAppRocketProperties mockProperties;
    @Mock
    private CloudAppRocketBeanFactory mockBeanFactory;
    
    private RocketRefreshComponent component;
    
    @BeforeEach
    void setUp() {
        component = new RocketRefreshComponent(mockProperties, mockBeanFactory);
    }
    
    @Test
    void testPostStart() {
        // Setup
        // Run the test
        component.postStart();
        
        // Verify the results
        verify(mockBeanFactory).refresh(any(CloudAppRocketProperties.class));
    }
    
    @Test
    void testPreStop() {
        component.preStop();
    }
    
    @Test
    void testBindKey() {
        assertEquals("io.cloudapp.messaging.rocketmq",
                     component.bindKey()
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("cloudAppRocket",
                     component.getName()
        );
    }
    
    @Test
    void testCreateBean() {
        // Setup
        final CloudAppRocketProperties properties = new CloudAppRocketProperties();
        properties.setEnabled(false);
        properties.setNameServer("nameServer");
        properties.setUsername("username");
        properties.setPassword("password");
        properties.setUseTLS(false);
        
        // Run the test
        final CloudAppRocketBeanFactory result = component.createBean(
                properties);
        
        // Verify the results
    }
    
}
