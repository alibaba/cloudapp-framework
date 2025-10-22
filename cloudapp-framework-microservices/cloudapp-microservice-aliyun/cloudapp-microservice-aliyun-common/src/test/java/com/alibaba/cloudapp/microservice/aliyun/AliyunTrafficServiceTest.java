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

package com.alibaba.cloudapp.microservice.aliyun;

import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.alibaba.cloudapp.util.FileUtils;
import io.opentelemetry.context.Scope;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class AliyunTrafficServiceTest {
    
    @InjectMocks
    private AliyunTrafficService trafficService;
    
    @Mock
    private NacosRegistration nacosRegistration;
    
    @Test
    public void getBaggageUserData_with_empty() {
        Assert.assertEquals(Collections.emptyMap(),
                            trafficService.getBaggageUserData()
        );
    }
    
    @Test
    public void getCurrentTrafficLabel_after_withTrafficLabel() {
        String testLabel = "testLabel";
        AliyunTrafficService spyTrafficService = Mockito.spy(trafficService);
        doReturn(testLabel).when(spyTrafficService).getCurrentTrafficLabel();
        Assert.assertEquals(testLabel,
                            spyTrafficService.getCurrentTrafficLabel());
    }
    
    @Test
    public void getCurrentTraceId() {
        Assert.assertNotNull(trafficService.currentTraceId());
    }
    
    @Test
    public void isDuringCanaryRelease() {
        Map<String, String> testAnnos = FileUtils.readPropertiesFromFile(
                "src/test/resources/annotations");
        Map<String, String> testLabels = FileUtils.readPropertiesFromFile(
                "src/test/resources/labels");
        trafficService.setInstanceAnnotations(testAnnos);
        trafficService.setInstanceLabels(testLabels);
        Assert.assertTrue(trafficService.isDuringCanaryRelease());
    }
    
    @Test
    public void isInstanceWarmingUp() {
        Map<String, String> immutableLabels =
                Collections.unmodifiableMap(new HashMap<String, String>() {{
                    put("ts", Long.toString(System.currentTimeMillis()));
                    put("t", Integer.toString(120));
                }});
        Mockito.when(nacosRegistration.getMetadata()).thenReturn(immutableLabels);
        Assert.assertTrue(trafficService.isInstanceWarmingUp());
    }
    
    @Test
    public void isInstanceWarmup_by_empty_metadata() {
        Mockito.when(nacosRegistration.getMetadata())
               .thenReturn(Collections.emptyMap());
        Assert.assertFalse(trafficService.isInstanceWarmingUp());
    }
    
    @Test
    public void getCurrentEnvironmentLabel() {
        Map<String, String> testLabels = FileUtils.readPropertiesFromFile(
                "src/test/resources/labels");
        trafficService.setInstanceLabels(testLabels);
        Assert.assertEquals(
                trafficService.getCurrentEnvironmentLabel(), "test");
    }
    
    @Test
    public void readPropertiesFromFile_not_accessible() {
        try {
            String tmpFilePath = "src/test/resources/cannot_read.tmp";
            File file = File.createTempFile("cannot_read", ".tmp");
            file.setReadable(false);
            tmpFilePath = file.getAbsolutePath();
            Map<String, String> canNotRead = FileUtils.readPropertiesFromFile(
                    tmpFilePath);
            Assert.assertEquals(Collections.emptyMap(), canNotRead);
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Map<String, String> notExist = FileUtils.readPropertiesFromFile(
                "src/test/resources/not_exist"
        );
        Assert.assertEquals(Collections.emptyMap(), notExist);
        Map<String, String> empty = FileUtils.readPropertiesFromFile(
                "src/test/resources/empty"
        );
        Assert.assertEquals(Collections.emptyMap(), empty);
    }
    
    @Test
    public void withTrafficLabel_with_empty_value() {
        Assert.assertNull(trafficService.getCurrentTrafficLabel());
    }
    
    @Test
    public void supportTrafficManagement() {
        System.setProperty("alicloud.deployment.mode", "EDAS_MANAGED");
        Assert.assertTrue(trafficService.supportTrafficManagement());
    }
    
    @Test
    public void currentTrafficMatchWith_by_notEmpty_label() {
        AliyunTrafficService spyTrafficService = Mockito.spy(trafficService);
        doReturn(true).when(spyTrafficService).currentTrafficMatchWith("test");
        Assert.assertTrue(spyTrafficService.currentTrafficMatchWith("test"));
    }
    
    @Test
    public void currentTrafficMatchWith_by_empty_label() {
        trafficService.withTrafficLabel(null);
        Assert.assertTrue(trafficService.currentTrafficMatchWith(null));
    }
    
    @Test
    public void getLabelValue_and_getAnnotationValue_return_default_value() {
        trafficService.setInstanceLabels(null);
        trafficService.setInstanceAnnotations(null);
        Assert.assertEquals(trafficService.isDuringCanaryRelease(), false);
        Assert.assertEquals(trafficService.getCurrentEnvironmentLabel(), "");
    }
    
    @Test
    public void getBaggageUserDataValue_found_and_notFound() {
        Map<String, String> immutableLabels =
                Collections.unmodifiableMap(new HashMap<String, String>() {{
                    put("test_baggage_key", "test_baggage_value");
                }});
        trafficService.withBaggageUserData(immutableLabels);
        // depend on arms-agent
//        Assert.assertEquals(trafficService.getBaggageUserDataValue(
//                "test_baggage_key"), "test_baggage_value");
        Assert.assertNull(trafficService.getBaggageUserDataValue(
                "test_baggage_key_not_exists"));
    }
    
}