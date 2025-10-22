package com.alibaba.cloudapp.starter.filestore.configuration;

import com.aliyun.oss.OSSClient;
import com.alibaba.cloudapp.starter.filestore.properties.OSSEndpointProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AliyunFileStoreComponentTest {
    
    @Mock
    private OSSEndpointProperties properties;
    @Mock
    private OSSClient bean;
    
    private AliyunFileStoreComponent storeComponent;
    
    @BeforeEach
    void setUp() {
        
        when(properties.getAccessKey()).thenReturn("accessKeyId");
        when(properties.getSecretKey()).thenReturn("secretAccessKey");
        when(properties.getStsToken()).thenReturn("stsToken");
        when(properties.getEndpoint()).thenReturn("endpoint");
        
        storeComponent = new AliyunFileStoreComponent(properties);
    }
    
    @Test
    void testPostStart() {
        // Setup
        
        // Run the test
        storeComponent.postStart();
        
        // Verify the results
    }
    
    @Test
    void testPreStop() {
        storeComponent.preStop();
    }
    
    @Test
    void testBindKey() {
        assertEquals("io.cloudapp.filestore.aliyun",
                     storeComponent.bindKey()
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("filestore.aliyun",
                     storeComponent.getName()
        );
    }
    
    @Test
    void testCreateBean() {
        
        // Run the test
        final OSSClient result = storeComponent.createBean(properties);
        
        // Verify the results
    }
    
}
