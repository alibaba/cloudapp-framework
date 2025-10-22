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

package com.alibaba.cloudapp.starter.messaging.properties;

import com.alibaba.cloudapp.messaging.rocketmq.properties.RocketConsumerProperties;
import com.alibaba.cloudapp.messaging.rocketmq.properties.RocketProducerProperties;
import com.alibaba.cloudapp.starter.base.properties.RefreshableProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(
        prefix = CloudAppRocketProperties.PREFIX,
        ignoreUnknownFields = false,
        ignoreInvalidFields = true
)
public class CloudAppRocketProperties extends RefreshableProperties {
    
    public static final String PREFIX = "io.cloudapp.messaging.rocketmq";

    private boolean enabled;

    private String nameServer;

    private String username;

    private String password;

    private boolean useTLS;

    private boolean enableMsgTrace;

    private String traceTopic;
    
    private String accessChannel = "ALIYUN";

    private List<RocketConsumerProperties> inputs;
    private List<RocketProducerProperties> outputs;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUseTLS() {
        return useTLS;
    }

    public void setUseTLS(boolean useTLS) {
        this.useTLS = useTLS;
    }

    public boolean isEnableMsgTrace() {
        return enableMsgTrace;
    }

    public void setEnableMsgTrace(boolean enableMsgTrace) {
        this.enableMsgTrace = enableMsgTrace;
    }

    public String getTraceTopic() {
        return traceTopic;
    }

    public void setTraceTopic(String traceTopic) {
        this.traceTopic = traceTopic;
    }

    public List<RocketConsumerProperties> getInputs() {
        return inputs;
    }

    public void setInputs(List<RocketConsumerProperties> inputs) {
        this.inputs = inputs;
    }

    public List<RocketProducerProperties> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<RocketProducerProperties> outputs) {
        this.outputs = outputs;
    }
    
    public String getAccessChannel() {
        return accessChannel;
    }
    
    public void setAccessChannel(String accessChannel) {
        this.accessChannel = accessChannel;
    }
}
