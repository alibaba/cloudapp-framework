package com.alibaba.cloudapp.starter.filestore.configuration;

import com.alibaba.cloudapp.filestore.minio.ClientProvider;
import com.alibaba.cloudapp.starter.filestore.properties.MinioEndpointProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MinioFileStoreComponentTest {
    
    @Mock
    private MinioEndpointProperties properties;
    
    private MinioFileStoreComponent storeComponent;
    
    @BeforeEach
    void setUp() {
        when(properties.getRegion()).thenReturn("result");
        when(properties.getEndpoint()).thenReturn("result");
        when(properties.getAccessKey()).thenReturn("result");
        when(properties.getSecretKey()).thenReturn("result");
        
        storeComponent = new MinioFileStoreComponent(properties);
        
    }
    
    @Test
    void testPostStart() {
        
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
        assertEquals("io.cloudapp.filestore.minio",
                     storeComponent.bindKey()
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("filestore.minio",
                     storeComponent.getName()
        );
    }
    
    @Test
    void testCreateBean() {
        // Run the test
        final ClientProvider result = storeComponent.createBean(
                properties);
        
        // Verify the results
    }
    
}
