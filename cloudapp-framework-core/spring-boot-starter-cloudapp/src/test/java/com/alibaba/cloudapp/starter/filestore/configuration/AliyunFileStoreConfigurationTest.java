package com.alibaba.cloudapp.starter.filestore.configuration;

import com.aliyun.oss.OSS;
import com.alibaba.cloudapp.exeption.CloudAppInvalidAccessException;
import com.alibaba.cloudapp.filestore.aliyun.service.*;
import com.alibaba.cloudapp.starter.filestore.properties.OSSEndpointProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AliyunFileStoreConfigurationTest {
    
    private AliyunFileStoreConfiguration storeConfiguration;
    
    private OSSEndpointProperties properties;
    
    @BeforeEach
    void setUp() {
        properties = new OSSEndpointProperties();
        properties.setAccessKey("accessKey");
        properties.setSecretKey("secretKey");
        properties.setRegion("region");
        properties.setEndpoint("endpoint");
        
        storeConfiguration = new AliyunFileStoreConfiguration();
        
    }
    
    @Test
    void testOssClient() {
        // Setup
        final AliyunFileStoreComponent fileStoreComponent = new AliyunFileStoreComponent(
                properties);
        
        // Run the test
        final OSS result = storeConfiguration.ossClient(
                fileStoreComponent);
        
        // Verify the results
        assert result != null;
    }
    
    @Test
    void testFileStoreComponent() {
        // Setup
        
        // Run the test
        final AliyunFileStoreComponent result = storeConfiguration.fileStoreComponent(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testFileStoreComponent_ThrowsCloudAppInvalidAccessException() {
        // Setup
        final OSSEndpointProperties properties = new OSSEndpointProperties();
        
        // Run the test
        assertThrows(CloudAppInvalidAccessException.class,
                     () -> storeConfiguration.fileStoreComponent(
                             properties)
        );
    }
    
    @Test
    void testOssBucketLifeCycleManager() {
        // Setup
        final OSS ossClient = null;
        
        // Run the test
        final OSSBucketLifeCycleManager result = storeConfiguration.ossBucketLifeCycleManager(
                ossClient);
        
        // Verify the results
    }
    
    @Test
    void testOssBucketManager() {
        // Setup
        final OSS ossClient = mock(OSS.class);
        
        // Run the test
        final OSSBucketManager result = storeConfiguration.ossBucketManager(
                ossClient);
        
        // Verify the results
    }
    
    @Test
    void testOssBucketPolicyManager() {
        // Setup
        final OSS ossClient = mock(OSS.class);
        
        // Run the test
        final OSSObjectPolicyManager result = storeConfiguration.ossBucketPolicyManager(
                ossClient);
        
        // Verify the results
    }
    
    @Test
    void testOssMultiPartsService() {
        // Setup
        final OSS ossClient = mock(OSS.class);
        
        // Run the test
        final OSSMultiPartsService result = storeConfiguration.ossMultiPartsService(
                ossClient);
        
        // Verify the results
    }
    
    @Test
    void testOssStorageObjectService() {
        // Setup
        final OSS ossClient = mock(OSS.class);
        
        // Run the test
        final OSSStorageObjectService result = storeConfiguration.ossStorageObjectService(
                ossClient);
        
        // Verify the results
    }
    
}
