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

package com.alibaba.cloudapp.messaging.rocketmq.properties;

import com.aliyun.openservices.ons.api.PropertyKeyConst;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RocketConsumerProperties {
    
    private String accessChannel;
    
    private String group;
    
    private String messageModel = "CLUSTERING";
    
    private int pullBatchSize = 10;
    
    private String namespace;
    
    private String name;
    
    private String topic;
    
    private List<String> tags = new ArrayList<>();
    
    private String nameServer;
    
    private String username;
    
    private String password;
    
    private boolean useTLS = false;
    
    private Boolean enableMsgTrace;
    
    private String traceTopic;
    
    private String type;
    
    private String securityToken;
    private int threadNum = 10;
    private int suspendTimeMillis = 30000;
    private int maxTimeout = 30000;
    private boolean vipChannelEnable = false;
    
    private boolean isDefault = false;
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getAccessChannel() {
        return accessChannel;
    }
    
    public void setAccessChannel(String accessChannel) {
        this.accessChannel = accessChannel;
    }
    
    public String getGroup() {
        return group;
    }
    
    public void setGroup(String group) {
        this.group = group;
    }
    
    public String getMessageModel() {
        return messageModel;
    }
    
    public void setMessageModel(String messageModel) {
        this.messageModel = messageModel;
    }
    
    public int getPullBatchSize() {
        return pullBatchSize;
    }
    
    public void setPullBatchSize(int pullBatchSize) {
        this.pullBatchSize = pullBatchSize;
    }
    
    
    public String getNamespace() {
        return namespace;
    }
    
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTopic() {
        return topic;
    }
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
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
    
    public Boolean isEnableMsgTrace() {
        return enableMsgTrace;
    }
    
    public void setEnableMsgTrace(Boolean enableMsgTrace) {
        this.enableMsgTrace = enableMsgTrace;
    }
    
    public String getTraceTopic() {
        return traceTopic;
    }
    
    public void setTraceTopic(String traceTopic) {
        this.traceTopic = traceTopic;
    }
    
    public boolean isDefault() {
        return isDefault;
    }
    
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    public Boolean getEnableMsgTrace() {
        return enableMsgTrace;
    }
    
    public String getSecurityToken() {
        return securityToken;
    }
    
    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }
    
    public int getThreadNum() {
        return threadNum;
    }
    
    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }
    
    public int getSuspendTimeMillis() {
        return suspendTimeMillis;
    }
    
    public void setSuspendTimeMillis(int suspendTimeMillis) {
        this.suspendTimeMillis = suspendTimeMillis;
    }
    
    public int getMaxTimeout() {
        return maxTimeout;
    }
    
    public void setMaxTimeout(int maxTimeout) {
        this.maxTimeout = maxTimeout;
    }
    
    public boolean isVipChannelEnable() {
        return vipChannelEnable;
    }
    
    public void setVipChannelEnable(boolean vipChannelEnable) {
        this.vipChannelEnable = vipChannelEnable;
    }
    
    public Properties toProperties() {
        Properties props = new Properties();
        if (username != null) {
            props.put(PropertyKeyConst.AccessKey, username);
        }
        if (password != null) {
            props.put(PropertyKeyConst.SecretKey, password);
        }
        if(group != null) {
            props.put(PropertyKeyConst.GROUP_ID, group);
        }
        if (securityToken != null) {
            props.put(PropertyKeyConst.SecurityToken, securityToken);
        }
        if(nameServer != null) {
            props.put(PropertyKeyConst.NAMESRV_ADDR, nameServer);
        }
        props.put(PropertyKeyConst.ConsumeThreadNums, threadNum);
        if (accessChannel != null) {
            props.put(PropertyKeyConst.OnsChannel, accessChannel);
        }
        props.put(PropertyKeyConst.SuspendTimeMillis, suspendTimeMillis);
        props.put(PropertyKeyConst.ConsumeTimeout, maxTimeout);
        if (name != null) {
            props.put(PropertyKeyConst.InstanceName, name);
        }
        if (enableMsgTrace != null) {
            props.put(PropertyKeyConst.MsgTraceSwitch, enableMsgTrace);
        }
        if (namespace != null) {
            props.put(PropertyKeyConst.INSTANCE_ID, namespace);
        }
        props.put(PropertyKeyConst.MAX_BATCH_MESSAGE_COUNT, pullBatchSize);
        props.put(PropertyKeyConst.ENABLE_ORDERLY_CONSUME_ACCELERATOR,
                  pullBatchSize
        );
        props.put(PropertyKeyConst.isVipChannelEnabled, vipChannelEnable);
        return props;
    }
    
}
