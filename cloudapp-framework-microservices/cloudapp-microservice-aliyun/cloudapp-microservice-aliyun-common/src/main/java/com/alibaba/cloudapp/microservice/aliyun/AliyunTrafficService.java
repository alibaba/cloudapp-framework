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
import com.alibaba.cloudapp.api.microservice.TrafficService;
import com.alibaba.cloudapp.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class AliyunTrafficService implements TrafficService,
        InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(
            AliyunTrafficService.class);
    
    private static ScheduledExecutorService executor =
            Executors.newSingleThreadScheduledExecutor();
    
    /**
     * The value of this property should be the same as the property value of
     * {@code alicloud.deployment.mode}.
     */
    private static final String EDAS_MANAGED = "EDAS_MANAGED";
    
    /**
     * The instance labels, parsed from /etc/podinfo/labels.
     */
    private static Map<String, String> instanceLabels;
    
    /**
     * The instance annotations, parsed from /etc/podinfo/annotations.
     */
    private static Map<String, String> instanceAnnotations;
    
    @Autowired(required = false)
    private NacosRegistration nacosRegistration;
    
    /**
     * For those instance which managed inside EDAS, will be started with the
     * JVM property "-Dalicloud.deployment.mode=EDAS_MANAGED".
     */
    @Override
    public boolean supportTrafficManagement() {
        String deploymentMode = System.getProperty("alicloud.deployment.mode");
        
        if (logger.isTraceEnabled()) {
            logger.trace("The alicloud.deployment.mode property value is {}",
                         deploymentMode
            );
        }
        
        return EDAS_MANAGED.equalsIgnoreCase(deploymentMode);
    }
    
    @Override
    public boolean currentTrafficMatchWith(String labelValue) {
        String tag = getCurrentTrafficLabel();
        if (logger.isDebugEnabled()) {
            logger.debug("Canary Tag is {}, and label value is: {}",
                         tag, labelValue
            );
        }
        
        if (!StringUtils.hasText(tag)) {
            return !StringUtils.hasText(labelValue);
        }
        
        return tag.equalsIgnoreCase(labelValue);
    }
    
    @Override
    public String getCurrentTrafficLabel() {
        // first get lane tag, then get canary tag
        String tag = ArmsTagTool.originalTrafficTag(
                ArmsTagTool.LANE_TAG);
        if (!StringUtils.hasText(tag)) {
            tag = ArmsTagTool.originalTrafficTag(
                    ArmsTagTool.CANARY_TAG);
        }
        return tag;
    }
    
    @Override
    public String getCurrentEnvironmentLabel() {
        return getLabelValue(
                "edas.alibabacloud.com/version", "");
    }
    
    private String getLabelValue(String key, String defaultValue) {
        if (instanceLabels == null) {
            if (logger.isDebugEnabled()) {
                logger.trace("The instance labels is null," +
                                     "return default value: {}", defaultValue);
            }
            return defaultValue;
        }
        
        return instanceLabels.getOrDefault(key, defaultValue);
    }
    
    private String getAnnotationValue(String key, String defaultValue) {
        if (instanceAnnotations == null) {
            if (logger.isDebugEnabled()) {
                logger.trace("The instance annotations is null, " +
                                     "return default value: {}", defaultValue);
            }
            return defaultValue;
        }
        
        return instanceAnnotations.getOrDefault(key, defaultValue);
    }
    
    
    @Override
    public boolean isDuringCanaryRelease() {
        String canaryRelease = getAnnotationValue("edas.canary",
                                                  "false"
        );
        return Boolean.parseBoolean(canaryRelease);
    }
    
    @Override
    public boolean isInstanceWarmingUp() {
        if (nacosRegistration == null) {
            logger.warn("The nacosRegistration is null, return false");
            return false;
        }
        Map<String, String> metadata = nacosRegistration.getMetadata();
        if (metadata == null || !containsWarmupKeys(metadata)) {
            logger.warn(
                    "The registration instance dose not contain warmup keys, " +
                            "return false");
            return false;
        }
        return isInstanceDuringWarmup(metadata, nacosRegistration.getHost());
    }
    
    private static boolean isInstanceDuringWarmup(Map<String, String> metadata,
                                                  String host) {
        if (metadata == null) {
            logger.warn("The metadata is null, return false");
            return false;
        }
        
        String ts = metadata.get("ts");
        String t = metadata.get("t");
        
        long time = Long.parseLong(ts);
        long warmupTime = Long.parseLong(t);
        long warmupTimePassed = System.currentTimeMillis() - time;
        
        if (warmupTimePassed < warmupTime) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "The instance {} is warming up, the warmup time left: {}",
                        host, warmupTime - warmupTimePassed
                );
            }
            return true;
        }
        return false;
    }
    
    private boolean containsWarmupKeys(Map<String, String> mt) {
        return mt != null && mt.containsKey("ts") && mt.containsKey("t");
    }
    
    @Override
    public void withTrafficLabel(String labelValue) {
        if (StringUtils.isEmpty(labelValue)) {
            logger.warn(
                    "The label value transferred from upstream is empty, which " +
                            "will lead to the traffic label erased," +
                            " please confirm is the desired behavior ?");
            ArmsTagTool.clearCanaryTag();
            return;
        }
        ArmsTagTool.withCanaryTag(labelValue);
    }
    
    @Override
    public Map<String, String> getBaggageUserData() {
        return ArmsTagTool.baggageItems();
    }
    
    @Override
    public String getBaggageUserDataValue(String key) {
        Map<String, String> baggageItems = ArmsTagTool.baggageItems();
        
        if (baggageItems.containsKey(key)) {
            return baggageItems.get(key);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "The baggage item with key {} is not found in the current " +
                            "span", key);
        }
        
        return null;
    }
    
    @Override
    public void withBaggageUserData(Map<String, String> pairs) {
        ArmsTagTool.putBaggageItems(pairs);
    }
    
    @Override
    public String currentTraceId() {
        return ArmsTagTool.getTraceId();
    }
    
    @Override
    public void afterPropertiesSet() {
        refreshInstanceEnv();
        
        if (supportTrafficManagement()) {
            executor.scheduleAtFixedRate(
                    this::refreshInstanceEnv, 5, 5,
                    TimeUnit.SECONDS
            );
            logger.info("Start refresh instance env every 5 seconds");
        }
    }
    
    private void refreshInstanceEnv() {
        try {
            instanceLabels = parseProperties(FileUtils.readPropertiesFromFile(
                    "/etc/podinfo/labels"));
            instanceAnnotations = parseProperties(FileUtils.readPropertiesFromFile(
                    "/etc/podinfo/annotations"));
        } catch (Throwable t) {
            logger.error("failed refresh instance env", t);
        }
    }
    
    private Map<String, String> parseProperties(Map props) {
        if (props == null || props.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<>();
        for (Object keyObject : props.keySet()) {
            String key = keyObject.toString();
            String value = props.get(key).toString();
            if (key.contains("\"")) {
                key = key.replaceAll("\"", "");
            }
            if (value.contains("\"")) {
                value = value.replaceAll("\"", "");
            }
            result.put(key, value);
        }
        return result;
    }
    
    @Override
    public void destroy() {
        logger.info("Stopping the traffic service.");
        if (supportTrafficManagement()) {
            executor.shutdown();
        }
    }
    
    public void setInstanceLabels(Map<String, String> instanceLabels) {
        this.instanceLabels = instanceLabels;
    }
    
    public void setInstanceAnnotations(Map<String, String> instanceAnnotations) {
        this.instanceAnnotations = instanceAnnotations;
    }
    
}
