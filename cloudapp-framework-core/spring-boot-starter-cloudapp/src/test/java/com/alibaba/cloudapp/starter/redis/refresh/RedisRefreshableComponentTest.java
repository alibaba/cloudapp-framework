package com.alibaba.cloudapp.starter.redis.refresh;

import com.alibaba.cloudapp.starter.redis.properties.CloudAppRedisProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RedisRefreshableComponentTest {
    
    private CloudAppRedisProperties properties;
    
    private RedisRefreshableComponent component;
    
    @BeforeEach
    void setUp() {
        properties = new CloudAppRedisProperties();
        properties.setBase(new RedisProperties());
        
        component = new RedisRefreshableComponent(properties);
    }
    
    @Test
    void testPostStart() {
        // Setup
        // Run the test
        component.postStart();
        
        // Verify the results
    }
    
    @Test
    void testPreStop() {
        component.preStop();
    }
    
    @Test
    void testBindKey() {
        assertEquals("io.cloudapp.redis",
                     component.bindKey()
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("cloudAppRedis",
                     component.getName()
        );
    }
    
    @Test
    void testCreateBean() {
        // Setup
        final CloudAppRedisProperties properties = new CloudAppRedisProperties();
        properties.setHashMonitoring(false);
        properties.setValueMonitoring(false);
        properties.setKeyEncrypt(false);
        final RedisProperties base = new RedisProperties();
        base.setClientType(RedisProperties.ClientType.LETTUCE);
        properties.setBase(base);
        
        // Run the test
        final RedisConnectionFactory result = component.createBean(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testCreateFactory() {
        // Setup
        final CloudAppRedisProperties properties = new CloudAppRedisProperties();
        properties.setHashMonitoring(false);
        properties.setValueMonitoring(false);
        properties.setKeyEncrypt(false);
        final RedisProperties base = new RedisProperties();
        base.setClientType(RedisProperties.ClientType.LETTUCE);
        properties.setBase(base);
        
        // Run the test
        final RedisConnectionFactory result = component.createFactory(
                properties);
        
        // Verify the results
    }
    
}
