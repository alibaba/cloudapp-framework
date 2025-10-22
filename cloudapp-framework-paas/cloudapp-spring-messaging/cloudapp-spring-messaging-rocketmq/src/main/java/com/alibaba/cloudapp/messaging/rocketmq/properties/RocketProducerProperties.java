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


public class RocketProducerProperties {

    private String group;

    private String name;

    private String namespace;

    private int sendTimeout = 3000;

    private boolean retryNextServer = false;

    private int compressMsgBodyOverHowMuch = 1024 * 4;

    private int maxMessageSize = 1024 * 1024 * 4;

    private int retryTimesWhenSendFailed = 2;

    private String nameServer;

    private String username;

    private String password;

    private boolean useTLS = false;

    private Boolean enableMsgTrace;

    private String traceTopic;

    private boolean isDefault = false;
    
    private boolean vipChannelEnable = false;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int getSendTimeout() {
        return this.sendTimeout;
    }

    public boolean getRetryNextServer() {
        return this.retryNextServer;
    }

    public int getCompressMsgBodyOverHowMuch() {
        return this.compressMsgBodyOverHowMuch;
    }

    public int getMaxMessageSize() {
        return this.maxMessageSize;
    }

    public int getRetryTimesWhenSendAsyncFailed() {
        return this.retryTimesWhenSendFailed;
    }

    public int getRetryTimesWhenSendFailed() {
        return this.retryTimesWhenSendFailed;
    }

    public void setSendTimeout(int sendTimeout) {
        this.sendTimeout = sendTimeout;
    }

    public void setRetryNextServer(boolean retryNextServer) {
        this.retryNextServer = retryNextServer;
    }

    public void setCompressMsgBodyOverHowMuch(int compressMsgBodyOverHowMuch) {
        this.compressMsgBodyOverHowMuch = compressMsgBodyOverHowMuch;
    }

    public void setMaxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }

    public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
    }

    public boolean isRetryNextServer() {
        return retryNextServer;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    public boolean isVipChannelEnable() {
        return vipChannelEnable;
    }
    
    public void setVipChannelEnable(boolean vipChannelEnable) {
        this.vipChannelEnable = vipChannelEnable;
    }
}
