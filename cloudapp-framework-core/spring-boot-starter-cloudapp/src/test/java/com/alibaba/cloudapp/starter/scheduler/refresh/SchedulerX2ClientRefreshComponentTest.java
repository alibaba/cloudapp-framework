package com.alibaba.cloudapp.starter.scheduler.refresh;

import com.alibaba.schedulerx.worker.SchedulerxWorker;
import com.aliyun.schedulerx220190430.Client;
import com.alibaba.cloudapp.scheduler.schedulerx2.GlobalJobBeanRegistrar;
import com.alibaba.cloudapp.scheduler.schedulerx2.GlobalJobSyncManager;
import com.alibaba.cloudapp.starter.scheduler.properties.SchedulerX2WorkerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SchedulerX2ClientRefreshComponentTest {
    
    @Mock
    private ApplicationContext applicationContext;
    
    private SchedulerX2ClientRefreshComponent component;
    
    @BeforeEach
    void setUp() {
        
        SchedulerX2WorkerProperties properties = new SchedulerX2WorkerProperties();
        properties.setNamespace("namespace");
        properties.setGroupId("groupId");
        properties.setAppKey("appKey");
        properties.setAccessKeyId("accessKeyId");
        properties.setAccessKeySecret("accessKeySecret");
        properties.setEndpoint("endpoint");
        properties.setRegionId("regionId");
        properties.setOpenAPIEndpoint("openAPIEndpoint");
        properties.setEnabled(true);
        properties.setRefreshable("true");
        
        component = new SchedulerX2ClientRefreshComponent(properties);
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
        assertEquals("io.cloudapp.scheduler.schedulerx2-worker",
                     component.bindKey()
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("cloudAppSchedulerWorkClient",
                     component.getName()
        );
    }
    
    @Test
    void testCreateBean() {
        // Setup
        final SchedulerX2WorkerProperties prop = new SchedulerX2WorkerProperties();
        prop.setNamespace("namespace");
        prop.setGroupId("groupId");
        prop.setAppKey("appKey");
        prop.setAccessKeyId("accessKeyId");
        prop.setAccessKeySecret("accessKeySecret");
        prop.setEndpoint("endpoint");
        prop.setRegionId("regionId");
        prop.setOpenAPIEndpoint("openAPIEndpoint");
        
        // Run the test
        final Client result = component.createBean(prop);
        
        // Verify the results
    }
    
    @Test
    void testGetWorker() {
        final SchedulerxWorker result = component.getWorker();
    }
    
    @Test
    void testGetJobSyncManager() {
        final GlobalJobSyncManager result = component.getJobSyncManager();
    }
    
    @Test
    void testGetRegistrar() {
        final GlobalJobBeanRegistrar result = component.getRegistrar();
    }
    
}
