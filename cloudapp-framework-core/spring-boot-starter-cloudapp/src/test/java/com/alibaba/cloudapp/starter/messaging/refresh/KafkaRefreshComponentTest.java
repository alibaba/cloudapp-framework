package com.alibaba.cloudapp.starter.messaging.refresh;

import com.alibaba.cloudapp.starter.messaging.factory.CloudAppKafkaBeanFactory;
import com.alibaba.cloudapp.starter.messaging.properties.CloudAppKafkaProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaRefreshComponentTest {
    
    @Mock
    private CloudAppKafkaProperties mockProperties;
    @Mock
    private CloudAppKafkaBeanFactory mockBeanFactory;
    
    private KafkaRefreshComponent component;
    
    @BeforeEach
    void setUp() {
        component = new KafkaRefreshComponent(
                mockProperties, mockBeanFactory);
    }
    
    @Test
    void testPostStart() {
        // Setup
        // Run the test
        component.postStart();
        
        // Verify the results
        verify(mockBeanFactory).refresh(any(CloudAppKafkaProperties.class));
    }
    
    @Test
    void testPreStop() {
        component.preStop();
    }
    
    @Test
    void testBindKey() {
        assertEquals("io.cloudapp.messaging.kafka",
                     component.bindKey()
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("cloudAppKafka", component.getName());
    }
    
    @Test
    void testCreateBean() {
        // Setup
        final CloudAppKafkaProperties properties = new CloudAppKafkaProperties();
        properties.setEnabled(false);
        properties.setMechanism("mechanism");
        properties.setServers("servers");
        final KafkaProperties.Ssl ssl = new KafkaProperties.Ssl();
        ssl.setKeyPassword("keyPassword");
        properties.setSsl(ssl);
        
        // Run the test
        final CloudAppKafkaBeanFactory result = component.createBean(
                properties);
        
        // Verify the results
    }
    
}
