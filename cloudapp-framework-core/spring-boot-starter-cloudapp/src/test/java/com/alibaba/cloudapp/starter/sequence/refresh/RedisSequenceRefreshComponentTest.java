package com.alibaba.cloudapp.starter.sequence.refresh;

import com.alibaba.cloudapp.starter.sequence.properties.CloudAppSequenceProperties;
import com.alibaba.cloudapp.starter.sequence.properties.RedisSequenceProperties;
import com.alibaba.cloudapp.starter.sequence.properties.SnowflakeProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RedisSequenceRefreshComponentTest {
    
    private CloudAppSequenceProperties properties;
    @Mock
    private ApplicationContext applciationContext;
    
    private RedisSequenceRefreshComponent component;
    
    @BeforeEach
    void setUp() {
        properties = new CloudAppSequenceProperties();
        properties.setRedis(new RedisSequenceProperties());
        properties.setSnowflake(new SnowflakeProperties());
        
        component = new RedisSequenceRefreshComponent(properties);
        component.setApplicationContext(applciationContext);
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
        assertEquals("io.cloudapp.sequence.redis",
                     component.bindKey()
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("sequenceRedisConnection",
                     component.getName()
        );
    }
    
    @Test
    void testCreateBean() {
        // Setup
        final CloudAppSequenceProperties properties = new CloudAppSequenceProperties();
        final SnowflakeProperties snowflake = new SnowflakeProperties();
        snowflake.setWorkerId(0L);
        snowflake.setWorkerIdBits(0L);
        properties.setSnowflake(snowflake);
        final RedisSequenceProperties redis = new RedisSequenceProperties();
        redis.setClientType(RedisProperties.ClientType.LETTUCE);
        properties.setRedis(redis);
        
        // Run the test
        final RedisConnectionFactory result = component.createBean(
                properties);
        
        // Verify the results
    }
    
}
