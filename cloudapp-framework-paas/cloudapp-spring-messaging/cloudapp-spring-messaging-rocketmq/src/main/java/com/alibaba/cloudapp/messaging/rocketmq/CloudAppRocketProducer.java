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

package com.alibaba.cloudapp.messaging.rocketmq;

import com.alibaba.cloudapp.api.messaging.Producer;
import com.alibaba.cloudapp.api.messaging.model.Destination;
import com.alibaba.cloudapp.api.messaging.model.MQMessage;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.messaging.rocketmq.model.RocketDestination;
import com.alibaba.cloudapp.messaging.rocketmq.properties.RocketProducerProperties;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.trace.AsyncTraceDispatcher;
import org.apache.rocketmq.client.trace.TraceDispatcher;
import org.apache.rocketmq.client.trace.hook.SendMessageTraceHookImpl;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class CloudAppRocketProducer implements
        Producer<MQProducer, MessageExt>, InitializingBean {
    
    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppRocketProducer.class);
    
    private RocketProducerProperties properties;
    
    private DefaultMQProducer producer;
    
    private final AtomicBoolean started = new AtomicBoolean(false);
    private String nameServerAddr;
    private SessionCredentials sessionCredentials;
    private String namespace;
    private TraceDispatcher traceDispatcher;
    
    public CloudAppRocketProducer(RocketProducerProperties properties) {
        this.properties = properties;
        this.producer = createProducer();
    }
    
    @Override
    public MQProducer getDelegatingProducer() {
        return producer;
    }
    
    @Override
    public void send(MQMessage<? extends MessageExt> message)
            throws CloudAppException {
        Destination destination = message.getDestination();
        this.send(destination, message.getMessageBody());
    }
    
    @Override
    public void send(Destination destination, MessageExt messageExt)
            throws CloudAppException {
        RocketDestination rd = destination instanceof RocketDestination ?
                (RocketDestination) destination :
                new RocketDestination(destination.getTopic());
        
        messageExt.setTopic(rd.getTopic());
        messageExt.setTags(rd.getTagsString());
        
        try {
            producer.send(messageExt);
            
        } catch (Exception e) {
            String msg = String.format("Failed to send message to topic %s",
                                       destination.getTopic()
            );
            
            throw new CloudAppException(msg, "CloudApp.MQSendFailed", e);
        }
    }
    
    @Override
    public void send(String topic, String message) throws CloudAppException {
        
        Message msg = new Message(
                topic, message.getBytes(StandardCharsets.UTF_8)
        );
        
        try {
            producer.send(msg);
            
        } catch (Exception e) {
            String errMsg = String.format(
                    "Failed to send message to topic %s", topic
            );
            
            throw new CloudAppException(errMsg, "CloudApp.MQSendFailed", e);
        }
    }
    
    @Override
    public void send(Destination destination, String message)
            throws CloudAppException {
        
        RocketDestination rd = destination instanceof RocketDestination ?
                (RocketDestination) destination :
                new RocketDestination(destination.getTopic());
        
        Message msg = new Message(rd.getTopic(),
                                  rd.getTagsString(),
                                  "",
                                  message.getBytes(StandardCharsets.UTF_8)
        );
        
        try {
            producer.send(msg);
            
        } catch (Exception e) {
            String errorMsg = String.format(
                    "Failed to send message to topic %s", destination.getTopic()
            );
            
            throw new CloudAppException(errorMsg, "CloudApp.MQSendFailed", e);
        }
    }
    
    @Override
    public CompletableFuture<SendResult> sendAsync(MQMessage<?
            extends MessageExt> message)
            throws CloudAppException {
        return sendAsync(message.getDestination(), message.getMessageBody());
    }
    
    @Override
    public CompletableFuture<SendResult> sendAsync(Destination destination,
                                                   MessageExt messageExt)
            throws CloudAppException {
        RocketDestination rd = destination instanceof RocketDestination ?
                (RocketDestination) destination :
                new RocketDestination(destination.getTopic());
        
        messageExt.setTags(rd.getTagsString());
        messageExt.setTopic(rd.getTopic());
        
        return getSendFutureResult(destination, messageExt);
    }
    
    @Override
    public CompletableFuture<SendResult> sendAsync(String topic, String message)
            throws CloudAppException {
        
        Message msg = new Message(
                topic, message.getBytes(StandardCharsets.UTF_8)
        );
        
        CompletableFuture<SendResult> future = new CompletableFuture<>();
        
        try {
            producer.send(msg, new SendCallback() {
                
                @Override
                public void onSuccess(SendResult sendResult) {
                    future.complete(sendResult);
                }
                
                @Override
                public void onException(Throwable e) {
                    future.completeExceptionally(e);
                }
            });
        } catch (Exception e) {
            String errorMsg = String.format(
                    "Failed to send message to topic %s", topic
            );
            throw new CloudAppException(errorMsg, "CloudApp.MQSendFailed", e);
        }
        return future;
    }
    
    @Override
    public CompletableFuture<SendResult> sendAsync(Destination destination,
                                                   String message)
            throws CloudAppException {
        
        RocketDestination rd = destination instanceof RocketDestination ?
                (RocketDestination) destination :
                new RocketDestination(destination.getTopic());
        
        Message msg = new Message(rd.getTopic(),
                                  rd.getTagsString(),
                                  "",
                                  message.getBytes(StandardCharsets.UTF_8)
        );
        
        return getSendFutureResult(destination, msg);
    }
    
    private CompletableFuture<SendResult> getSendFutureResult(Destination destination,
                                                              Message msg) {
        CompletableFuture<SendResult> future = new CompletableFuture<>();
        
        try {
            producer.send(msg, new SendCallback() {
                
                @Override
                public void onSuccess(SendResult sendResult) {
                    future.complete(sendResult);
                }
                
                @Override
                public void onException(Throwable e) {
                    future.completeExceptionally(e);
                }
            });
            
        } catch (Exception e) {
            String errorMsg = String.format(
                    "Failed to send message to topic %s", destination.getTopic()
            );
            
            throw new CloudAppException(errorMsg, "CloudApp.MQSendFailed", e);
        }
        
        return future;
    }
    
    @Override
    public void close() {
        producer.shutdown();
    }
    
    public void start() {
        if (this.started.compareAndSet(false, true)) {
            try {
                producer.start();
            } catch (MQClientException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public void shutdown() {
        if (this.started.compareAndSet(true, false)) {
            producer.shutdown();
        }
    }
    
    public DefaultMQProducer createProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(
                properties.getGroup(),
                new AclClientRPCHook(sessionCredentials)
        );
        
        producer.setNamespaceV2(properties.getNamespace());
        producer.setProducerGroup(properties.getGroup());
        producer.setSendMsgTimeout(properties.getSendTimeout() * 1000);
        
        String instanceName = StringUtils.hasText(properties.getName()) ?
                properties.getName() : this.buildInstanceName();
        producer.setInstanceName(instanceName);
        producer.setNamesrvAddr(nameServerAddr);
        producer.setMaxMessageSize(1024 * 1024 * 4);
        producer.setVipChannelEnabled(false);
        if (!properties.isEnableMsgTrace()) {
            logger.info("MQ Client Disable the Trace Hook!");
        } else {
            AsyncTraceDispatcher dispatcher = new AsyncTraceDispatcher(
                    properties.getGroup(),
                    TraceDispatcher.Type.CONSUME,
                    properties.getTraceTopic(),
                    null
            );
            dispatcher.getTraceProducer().setUseTLS(properties.isUseTLS());
            this.traceDispatcher = dispatcher;
            producer.getDefaultMQProducerImpl()
                    .registerSendMessageHook(
                            new SendMessageTraceHookImpl(traceDispatcher)
                    );
        }
        return producer;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Starting RocketMQ producer");
    }
    
    public void refresh(RocketProducerProperties properties) {
        synchronized (this) {
            boolean isStarted = this.isStarted();
            
            if (isStarted) {
                this.shutdown();
            }
            
            this.properties = properties;
            producer = createProducer();
            this.traceDispatcher = null;
            if  (isStarted) {
                this.start();
            }
        }
    }
    
    protected void updateNameServerAddr(String newAddrs) {
        logger.info("updateNameServerAddr {}", newAddrs);
    }
    
    public boolean isStarted() {
        return started.get();
    }
    
    public void setStarted(boolean started) {
        this.started.set(started);
    }
    
    private String buildInstanceName() {
        long timestamp = System.nanoTime();
        return Long.toHexString(timestamp);
    }
}
