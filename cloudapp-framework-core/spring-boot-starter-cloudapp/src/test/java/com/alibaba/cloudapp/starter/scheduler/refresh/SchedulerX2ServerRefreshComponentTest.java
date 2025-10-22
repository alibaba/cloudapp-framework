package com.alibaba.cloudapp.starter.scheduler.refresh;

import com.aliyun.schedulerx220190430.Client;
import com.alibaba.cloudapp.starter.scheduler.properties.SchedulerX2ServerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SchedulerX2ServerRefreshComponentTest {
    
    private SchedulerX2ServerRefreshComponent component;
    @Mock
    private ApplicationContext applicationContext;
    
    @BeforeEach
    void setUp() {
        SchedulerX2ServerProperties properties = new SchedulerX2ServerProperties();
        properties.setEnabled(false);
        properties.setAccessKeyId("accessKeyId");
        properties.setAccessKeySecret("accessKeySecret");
        properties.setEndpoint("endpoint");
        properties.setRegionId("regionId");
        properties.setRefreshable("true");
        properties.setRefreshable("true");
        properties.setRefreshable("true");
        
        component = new SchedulerX2ServerRefreshComponent(properties);
        component.setApplicationContext(applicationContext);
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
        assertEquals("io.cloudapp.scheduler.schedulerx2-server",
                     component.bindKey()
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("cloudAppSchedulerX2ServerClient",
                     component.getName()
        );
    }
    
    @Test
    void testCreateBean() {
        // Setup
        final SchedulerX2ServerProperties properties = new SchedulerX2ServerProperties();
        properties.setEnabled(false);
        properties.setAccessKeyId("accessKeyId");
        properties.setAccessKeySecret("accessKeySecret");
        properties.setEndpoint("endpoint");
        properties.setRegionId("regionId");
        
        // Run the test
        final Client result = component.createBean(
                properties);
        
        // Verify the results
    }
    
}
