package com.alibaba.cloudapp.starter.filestore.configuration;

import com.alibaba.cloudapp.filestore.minio.ClientProvider;
import com.alibaba.cloudapp.filestore.minio.service.*;
import com.alibaba.cloudapp.starter.filestore.properties.MinioEndpointProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MinioFileStoreConfigurationTest {
    
    private MinioFileStoreConfiguration configuration;
    
    @Mock
    private MinioEndpointProperties properties;
    private ClientProvider minioClient;
    
    @BeforeEach
    void setUp() {
        
        when(properties.getRegion()).thenReturn("result");
        when(properties.getEndpoint()).thenReturn("result");
        when(properties.getAccessKey()).thenReturn("result");
        when(properties.getSecretKey()).thenReturn("result");
        
        configuration = new MinioFileStoreConfiguration();
        
        MinioFileStoreComponent component = configuration.minioFileStoreComponent(
                properties);
        minioClient = configuration.minioClient(component);
    }
    
    @Test
    void testMinioFileStoreComponent() {
        // Setup
        
        // Run the test
        final MinioFileStoreComponent result = configuration.minioFileStoreComponent(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testMinioFileStoreComponent_ThrowsCloudAppInvalidAccessException() {
        // Setup
        MinioEndpointProperties properties = new MinioEndpointProperties();
        
        // Run the test
        assertThrows(IllegalArgumentException.class,
                     () -> configuration.minioFileStoreComponent(
                             properties)
        );
    }
    
    @Test
    void testMinioBucketManager() {
        // Setup
    
        // Run the test
        final MinioBucketManager result = configuration.minioBucketManager(
                minioClient);
        
        // Verify the results
    }
    
    @Test
    void testMinioBucketLifeCycleManager() {
        // Setup
        
        // Run the test
        final MinioBucketLifeCycleManager result = configuration.minioBucketLifeCycleManager(
                minioClient);
        
        // Verify the results
    }
    
    @Test
    void testMinioObjectPolicyManager() {
        // Setup
        
        // Run the test
        final MinioObjectPolicyManager result = configuration.minioObjectPolicyManager(
                minioClient);
        
        // Verify the results
    }
    
    @Test
    void testMinioMultiPartsService() {
        // Setup
        
        // Run the test
        final MinioMultiPartsService result = configuration.minioMultiPartsService(
                minioClient);
        
        // Verify the results
    }
    
    @Test
    void testMinioStorageObjectService() {
        // Setup
        
        // Run the test
        final MinioStorageObjectService result = configuration.minioStorageObjectService(
                minioClient);
        
        // Verify the results
    }
    
}
