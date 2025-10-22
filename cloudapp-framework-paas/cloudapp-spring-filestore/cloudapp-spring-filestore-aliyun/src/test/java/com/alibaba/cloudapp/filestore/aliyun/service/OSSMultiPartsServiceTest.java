/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.alibaba.cloudapp.filestore.aliyun.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.common.comm.ServiceClient;
import com.aliyun.oss.model.*;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.model.Pairs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OSSMultiPartsServiceTest {
    
    @Mock
    private OSS oss;
    
    private OSSMultiPartsService service;
    
    private static final String BUCKET_NAME = "test-bucket";
    private static final String OBJECT_NAME = "test-object";
    private static final String UPLOAD_ID = "test-upload-id";
    
    @Before
    public void setUp() {
        service = new OSSMultiPartsService(oss);
        
        InitiateMultipartUploadResult initiateMultipartUploadResult = new InitiateMultipartUploadResult();
        initiateMultipartUploadResult.setUploadId(UPLOAD_ID);
        when(oss.initiateMultipartUpload(any())).thenReturn(
                initiateMultipartUploadResult);
        
        VoidResult result = new VoidResult();
        ResponseMessage message = new ResponseMessage(
                new ServiceClient.Request());
        message.setStatusCode(200);
        result.setResponse(message);
        doReturn(result).when(oss).abortMultipartUpload(any());
        
        CompleteMultipartUploadResult completeResult = new CompleteMultipartUploadResult();
        completeResult.setResponse(message);
        doReturn(completeResult).when(oss).completeMultipartUpload(
                any(CompleteMultipartUploadRequest.class));
        
    }
    
    @Test
    public void uploadObjects_BucketDoesNotExist_ThrowsCloudAppInvalidRequestException() {
        List<Pairs.Pair<String, InputStream>> objects = Arrays.asList(
                new Pairs.Pair<>("part1",
                                 new ByteArrayInputStream("data1".getBytes())
                ),
                new Pairs.Pair<>("part2",
                                 new ByteArrayInputStream("data2".getBytes())
                )
        );
        
        when(oss.doesBucketExist(BUCKET_NAME)).thenReturn(false);
        
        assertThrows(CloudAppException.class,
                     () -> service.uploadObjects(BUCKET_NAME, OBJECT_NAME,
                                                 objects));
    }
    
    @Test
    public void uploadObjects_InitiateMultipartUploadFails_ThrowsCloudAppException() {
        List<Pairs.Pair<String, InputStream>> objects = Arrays.asList(
                new Pairs.Pair<>("part1",
                                 new ByteArrayInputStream("data1".getBytes())
                ),
                new Pairs.Pair<>("part2",
                                 new ByteArrayInputStream("data2".getBytes())
                )
        );
        
        when(oss.doesBucketExist(BUCKET_NAME)).thenReturn(true);
        when(oss.initiateMultipartUpload(
                any(InitiateMultipartUploadRequest.class))).thenReturn(null);
        
        assertThrows(CloudAppException.class,
                     () -> service.uploadObjects(BUCKET_NAME, OBJECT_NAME,
                                                 objects));
    }
    
    @Test
    public void uploadObjects_UploadPartFails_ThrowsCloudAppException() {
        List<Pairs.Pair<String, InputStream>> objects = Arrays.asList(
                new Pairs.Pair<>("part1",
                                 new ByteArrayInputStream("data1".getBytes())
                ),
                new Pairs.Pair<>("part2",
                                 new ByteArrayInputStream("data2".getBytes())
                )
        );
        
        when(oss.doesBucketExist(BUCKET_NAME)).thenReturn(true);
        
        doThrow(new RuntimeException("Upload failed")).when(oss).uploadPart(
                any(UploadPartRequest.class));
        
        assertThrows(CloudAppException.class,
                     () -> service.uploadObjects(BUCKET_NAME, OBJECT_NAME,
                                                 objects));
    }
    
    @Test
    public void uploadObjects_SuccessfulUpload_ReturnsTrue() {
        List<Pairs.Pair<String, InputStream>> objects = Arrays.asList(
                new Pairs.Pair<>("part1",
                                 new ByteArrayInputStream("data1".getBytes())
                ),
                new Pairs.Pair<>("part2",
                                 new ByteArrayInputStream("data2".getBytes())
                )
        );
        
        when(oss.doesBucketExist(BUCKET_NAME)).thenReturn(true);
        InitiateMultipartUploadResult initResult = new InitiateMultipartUploadResult();
        initResult.setUploadId("uploadId");
        when(oss.initiateMultipartUpload(
                any(InitiateMultipartUploadRequest.class))).thenReturn(
                initResult);
        when(oss.uploadPart(any(UploadPartRequest.class))).thenReturn(
                new UploadPartResult());
        
        Map<String, Integer> result = service.uploadObjects(BUCKET_NAME,
                                                            OBJECT_NAME, objects
        );
        
        assertNotNull(result);
        verify(oss, times(1))
                .completeMultipartUpload(
                        any(CompleteMultipartUploadRequest.class));
    }
    
    @Test
    public void uploadObjects_AbortMultipartUploadOnFailure() {
        List<Pairs.Pair<String, InputStream>> objects = Arrays.asList(
                new Pairs.Pair<>("part1",
                                 new ByteArrayInputStream("data1".getBytes())
                ),
                new Pairs.Pair<>("part2",
                                 new ByteArrayInputStream("data2".getBytes())
                )
        );
        
        when(oss.doesBucketExist(BUCKET_NAME)).thenReturn(true);
        InitiateMultipartUploadResult initResult = new InitiateMultipartUploadResult();
        initResult.setUploadId("uploadId");
        when(oss.initiateMultipartUpload(
                any(InitiateMultipartUploadRequest.class))).thenReturn(
                initResult);
        doThrow(new RuntimeException("Upload failed")).when(oss).uploadPart(
                any(UploadPartRequest.class));
        
        assertThrows(CloudAppException.class,
                     () -> service.uploadObjects(BUCKET_NAME, OBJECT_NAME,
                                                 objects
                     )
        );
        
        verify(oss, times(1)).abortMultipartUpload(
                any(AbortMultipartUploadRequest.class));
    }
    
    @Test
    public void testUploadBigFileSuccess() {
        // Setup
        when(oss.doesBucketExist(BUCKET_NAME)).thenReturn(true);
        
        // Execute
        boolean result = service.uploadBigFile(BUCKET_NAME, OBJECT_NAME,
                                               Paths.get(
                                                       "src/test/resources/test-file.txt"),
                                               5
        );
        
        // Verify
        assertTrue(result);
    }
    
    @Test(expected = CloudAppException.class)
    public void testUploadBigFileFailureBucketDoesNotExist() {
        // Setup
        when(oss.doesBucketExist(BUCKET_NAME)).thenReturn(false);
        
        // Execute
        service.uploadBigFile(BUCKET_NAME, OBJECT_NAME,
                              Paths.get("src/test/resources/test-file.txt"), 5
        );
    }
    
    @Test(expected = CloudAppException.class)
    public void testUploadBigFileEdgeCaseEmptyFileName() {
        // Execute
        boolean result = service.uploadBigFile(BUCKET_NAME, "",
                                               Paths.get(
                                                       "src/test/resources/test-file.txt"),
                                               5
        );
        
    }
    
    @Test(expected = CloudAppException.class)
    public void testUploadBigFileEdgeCaseZeroMaxSizePerPart() {
        // Setup
        when(oss.doesBucketExist(BUCKET_NAME)).thenReturn(true);
        
        // Execute
        boolean result = service.uploadBigFile(BUCKET_NAME, OBJECT_NAME,
                                               Paths.get(
                                                       "src/test/resources/test-file.txt"),
                                               0
        );
        
        // Verify
        assertFalse(
                result); // Expecting false, as zero max size should not proceed
    }
    
}
