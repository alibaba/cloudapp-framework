package com.alibaba.cloudapp.starter.sequence.refresh;

import com.alibaba.cloudapp.sequence.service.SnowflakeSequenceGenerator;
import com.alibaba.cloudapp.starter.sequence.properties.CloudAppSequenceProperties;
import com.alibaba.cloudapp.starter.sequence.properties.RedisSequenceProperties;
import com.alibaba.cloudapp.starter.sequence.properties.SnowflakeProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SnowflakeSequenceRefreshComponentTest {
    
    private CloudAppSequenceProperties properties;
    @Mock
    private ApplicationContext mockApplicationContext;
    
    private SnowflakeSequenceRefreshComponent component;
    
    @BeforeEach
    void setUp() {
        properties = new CloudAppSequenceProperties();
        properties.setSnowflake(new SnowflakeProperties());
        properties.setRedis(new RedisSequenceProperties());
        
        component = new SnowflakeSequenceRefreshComponent(properties);
        component.setApplicationContext(mockApplicationContext);
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
        assertEquals("io.cloudapp.sequence.snowflake",
                     component.bindKey()
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("snowflakeSequence",
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
        snowflake.setSequenceBits(0L);
        snowflake.setEnabled(false);
        properties.setSnowflake(snowflake);
        
        // Run the test
        final SnowflakeSequenceGenerator result = component.createBean(
                properties);
        
        // Verify the results
    }
    
}
