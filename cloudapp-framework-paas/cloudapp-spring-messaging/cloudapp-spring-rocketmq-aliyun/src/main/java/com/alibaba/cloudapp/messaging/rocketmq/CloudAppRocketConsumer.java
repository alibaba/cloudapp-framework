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

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.impl.rocketmq.ONSClientAbstract;
import com.aliyun.openservices.ons.api.impl.rocketmq.ONSUtil;
import com.aliyun.openservices.ons.api.impl.rocketmq.OnsClientRPCHook;
import com.aliyun.openservices.ons.api.impl.tracehook.OnsConsumeMessageHookImpl;
import com.aliyun.openservices.ons.api.impl.util.NameAddrUtils;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.*;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.exception.MQClientException;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.impl.consumer.DefaultMQPullConsumerImpl;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.impl.factory.MQClientInstance;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageQueue;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.protocol.NamespaceUtil;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.alibaba.cloudapp.api.messaging.Consumer;
import com.alibaba.cloudapp.api.messaging.Notifier;
import com.alibaba.cloudapp.api.messaging.ThreadLocalTraceStorage;
import com.alibaba.cloudapp.api.messaging.TraceStorage;
import com.alibaba.cloudapp.api.messaging.model.Destination;
import com.alibaba.cloudapp.api.messaging.model.Location;
import com.alibaba.cloudapp.api.messaging.model.MQMessage;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.messaging.rocketmq.model.RocketDestination;
import com.alibaba.cloudapp.messaging.rocketmq.model.RocketMQMessage;
import com.alibaba.cloudapp.messaging.rocketmq.properties.RocketConsumerProperties;
import com.alibaba.cloudapp.util.NetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class CloudAppRocketConsumer extends ONSClientAbstract implements
        InitializingBean, Consumer<MQPullConsumer, MessageExt> {
    
    private static final int SINGLE = 1;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);
    
    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppRocketConsumer.class);
    
    private RocketConsumerProperties consumerProperties;
    
    private TraceStorage traceStorage = new ThreadLocalTraceStorage();
    private String namespace;
    
    private static final Map<RocketDestination, Notifier<MessageExt>> CONSUMERS =
            Collections.synchronizedMap(new HashMap<>());
    
    /**
     * default consumer and subscribed all topic
     */
    private DefaultMQPullConsumer pullConsumer;
    private DefaultMQPushConsumer pushConsumer;
    
    public CloudAppRocketConsumer(RocketConsumerProperties properties) {
        super(ONSUtil.extractProperties(properties.toProperties()));
        this.namespace = getNamespace();
        this.consumerProperties = properties;
        this.pushConsumer = createPushConsumer();
        this.pullConsumer = createConsumer();
    }
    
    @Override
    public MQPullConsumer getDelegatingConsumer() {
        return pullConsumer;
    }
    
    @Override
    public MQMessage<? extends MessageExt> pull(Destination destination)
            throws CloudAppException {
        return this.pull(destination, DEFAULT_TIMEOUT);
    }
    
    @Override
    public Collection<MQMessage<? extends MessageExt>> pull(
            Destination destination, int count
    ) throws CloudAppException {
        return this.pull(destination, count, DEFAULT_TIMEOUT);
    }
    
    @Override
    public MessageExt pull(String topic) {
        List<MessageExt> list = pollTopicMessages(
                SINGLE, DEFAULT_TIMEOUT, createDestination(topic)
        );
        
        if (list.isEmpty()) {
            logger.info("No message from topic: {}", topic);
            return null;
        }
        
        return list.get(0);
    }
    
    @Override
    public Collection<MessageExt> pull(String topic, int count) {
        
        return pollTopicMessages(
                count, DEFAULT_TIMEOUT, createDestination(topic)
        );
    }
    
    @Override
    public MQMessage<? extends MessageExt> pull(
            Destination destination, Duration timeout
    ) {
        List<MessageExt> list = pollTopicMessages(
                SINGLE, DEFAULT_TIMEOUT, destination
        );
        
        if (list.isEmpty()) {
            logger.info("No message from topic: {}", destination.getTopic());
            return null;
        }
        
        return convertMessage(list.get(0), destination);
    }
    
    @Override
    public MessageExt pull(String topic, Duration timeout) {
        
        RocketDestination rd = createDestination(topic);
        
        List<MessageExt> list = pollTopicMessages(SINGLE, DEFAULT_TIMEOUT, rd);
        
        if (list.isEmpty()) {
            logger.info("No message from topic: {}", topic);
            return null;
        }
        
        return list.get(0);
    }
    
    
    @Override
    public Collection<MQMessage<? extends MessageExt>> pull(
            Destination destination, int count, Duration timeout
    ) {
        List<MessageExt> list = pollTopicMessages(count, timeout, destination);
        
        return list.stream()
           .map(e -> convertMessage(e, destination))
           .collect(Collectors.toList());
    }
    
    
    @Override
    public Collection<MessageExt> pull(
            String topic, int count, Duration timeout
    ) {
        return pollTopicMessages(count, timeout, createDestination(topic));
    }
    
    MQMessage<MessageExt> convertMessage(
            MessageExt m, Destination destination
    ) {
        if (m == null) {
            return null;
        }
        
        MQMessage<MessageExt> message = new RocketMQMessage();
        message.setMessageBody(m);
        message.setDestination(destination);
        message.setMessageID(m.getMsgId());
        message.setSentTimestamp(m.getBornTimestamp());
        message.setReceivedTimestamp(m.getStoreTimestamp());
        message.setDeliveredTimestamp(System.currentTimeMillis());
        message.setReceiver(createLocation(m.getBornHost()));
        message.setSender(createLocation(new InetSocketAddress(8080)));
        
        return message;
    }
    
    @Override
    public void subscribe(
            Destination destination, Notifier<MessageExt> notifier
    ) {
        if (notifier == null) {
            throw new CloudAppException("Notifier can not be null");
        }
        if (destination == null || destination.getTopic() == null
                || destination.getTopic().isEmpty()) {
            throw new CloudAppException("Topic can not be empty");
        }
        
        RocketDestination rd = destination instanceof RocketDestination ?
                (RocketDestination) destination :
                new RocketDestination(destination.getTopic());
        try {
            if (!isSubscribed(rd)) {
               pushConsumer.subscribe(rd.getTopic(), rd.getTagsString());
               CONSUMERS.put(rd, notifier);
            } else {
                throw new CloudAppException(
                        "Subscribing to topic '" + rd.getTopic() + "' multiple times");
            }
        } catch (Exception e) {
            throw new CloudAppException("subscribe failed", e);
        }
    }
    
    @Override
    public void subscribe(
            String topic, Notifier<MessageExt> notifier
    ) {
        this.subscribe(new RocketDestination(topic), notifier);
    }
    
    @Override
    public void unsubscribe(Destination destination) {
        this.unsubscribe(destination, null);
    }
    
    @Override
    public void unsubscribe(String topic) {
        this.unsubscribe(topic, null);
    }
    
    private boolean isSubscribed(RocketDestination destination) {
        return CONSUMERS.keySet().stream().anyMatch(
                d -> d.isContains(destination)
        );
    }
    
    private boolean isSubscribed(String topic, String tags) {
        RocketDestination destination = new RocketDestination(topic);
        destination.addTag(tags);
        return CONSUMERS.keySet().stream().anyMatch(
                d -> d.isContains(destination)
        );
    }
    
    @Override
    public void unsubscribe(
            Destination destination, Notifier<MessageExt> notifier
    ) {
        if (destination == null || destination.getTopic() == null
                || destination.getTopic().isEmpty()) {
            throw new CloudAppException("Topic can not be empty");
        }
        
        RocketDestination rd = destination instanceof RocketDestination ?
                (RocketDestination) destination :
                new RocketDestination(destination.getTopic());
        if (!CONSUMERS.containsKey(rd)) {
            throw new CloudAppException(
                    "Unsubscribing from topic '" + rd.getTopic());
        }
        CONSUMERS.remove(rd);
        
        if (notifier != null) {
            MessageExt message = new MessageExt();
            message.setMsgId(UUID.randomUUID().toString());
            message.setTopic(rd.getTopic());
            message.setBody("unsubscribed".getBytes());
            message.setTags(rd.getTagsString());
            message.setKeys("");
            message.setBornHost(new InetSocketAddress(8080));
            message.setStoreHost(new InetSocketAddress(8080));
            notifier.onMessageNotified(convertMessage(message, rd));
        }
    }
    
    @Override
    public void unsubscribe(
            String topic, Notifier<MessageExt> notifier
    ) {
        RocketDestination rd = createDestination(topic);
        
        this.unsubscribe(rd, notifier);
    }
    
    @Override
    public void close() {
        pullConsumer.shutdown();
    }
    
    private DefaultMQPullConsumer createConsumer() {
        try {
            String name = "pull" + consumerProperties.getName()
                    + new Random().nextInt(10000);
            DefaultMQPullConsumer consumer = new DefaultMQPullConsumer(
                    namespace, consumerProperties.getGroup(),
                    new OnsClientRPCHook(sessionCredentials)
            );
            consumer.setNamesrvAddr(nameServerAddr);
            consumer.setConsumerGroup(consumerProperties.getGroup());
            consumer.setInstanceName(name);
            consumer.setVipChannelEnabled(consumerProperties.isVipChannelEnable());
            consumer.setBrokerSuspendMaxTimeMillis(consumerProperties.getSuspendTimeMillis());
            consumer.setConsumerPullTimeoutMillis(consumerProperties.getMaxTimeout());
            return consumer;
        } catch (Exception e) {
            throw new CloudAppException("try create consumer failed.", e);
        }
    }
    
    private DefaultMQPushConsumer createPushConsumer() {
        try {
            String name = "push" + consumerProperties.getName()
                    + new Random().nextInt(10000);
            
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
                    namespace,
                    consumerProperties.getGroup(),
                    new OnsClientRPCHook(sessionCredentials)
            );
            
            if (consumerProperties.getPullBatchSize() > 0) {
                consumer.setPullBatchSize(consumerProperties.getPullBatchSize());
            }
            if (consumerProperties.getMaxTimeout() > 0) {
                try {
                    consumer.setConsumeTimeout(consumerProperties.getMaxTimeout());
                } catch (NumberFormatException ignored) {
                }
            }
            
            consumer.setInstanceName(name);
            consumer.setNamesrvAddr(this.getNameServerAddr());
            consumer.setVipChannelEnabled(consumerProperties.isVipChannelEnable());
            boolean msgTraceSwitch = consumerProperties.getEnableMsgTrace();
            if(msgTraceSwitch) {
                this.traceDispatcher = TracerUtil.createTraceDispatcher(
                        sessionCredentials,
                       this::getNameServerAddr,
                        consumer.getDefaultMQPushConsumerImpl()
                );
                consumer.getDefaultMQPushConsumerImpl()
                        .registerConsumeMessageHook(
                                new OnsConsumeMessageHookImpl(traceDispatcher)
                        );
            }
            
            consumer.setPostSubscriptionWhenPull(false);
            String messageModel = consumerProperties.getMessageModel();
            consumer.setMessageModel(MessageModel.valueOf(messageModel));
            return consumer;
        } catch (Exception e) {
            throw new CloudAppException("try create consumer failed.", e);
        }
    }
    
    private List<MessageExt> pollTopicMessages(
            int count, Duration timeout, Destination destination
    ) {
        
        String topic = destination.getTopic();
        String tag = destination instanceof RocketDestination ?
                ((RocketDestination) destination).getTagsString() : null;
        try {
            Set<MessageQueue> set = pullConsumer.fetchSubscribeMessageQueues(topic);
            List<MessageExt> list =
                    Collections.synchronizedList(new ArrayList<>(count));
            if(set.isEmpty()) {
                return list;
            }
            int pullCount = 0;
            for (MessageQueue queue : set) {
                try {
                    long offset = pullConsumer.fetchConsumeOffset(
                            queue,true);
                    PullResult pullResult = pullConsumer.pull(
                            queue, tag, offset < 0 ? 0 : offset, count,
                            timeout.toMillis()
                    );
                    pullConsumer.updateConsumeOffset(
                            queue, pullResult.getNextBeginOffset());
                    queue.setTopic(NamespaceUtil.wrapNamespace(
                            namespace, queue.getTopic()
                    ));
                    pullConsumer.getOffsetStore().updateConsumeOffsetToBroker(
                            queue, pullResult.getNextBeginOffset(), false);
                    if (pullResult.getPullStatus().equals(PullStatus.FOUND)) {
                        list.addAll(pullResult.getMsgFoundList());
                        pullCount = pullResult.getMsgFoundList().size();
                    }
                    count = count - pullCount;
                    if (pullCount >= count) {
                        break;
                    }
                } catch (Exception e) {
                    throw new CloudAppException("pull message error", e);
                }
            }
            return list;
        } catch (Exception e) {
            throw new CloudAppException("pull message error", e);
        }
    }
    
    /**
     * setting trace storage
     *
     * @param traceStorage trace storage
     */
    public void setTraceStorage(TraceStorage traceStorage) {
        this.traceStorage = traceStorage;
    }
    
    /**
     * get current trace storage
     *
     * @return trace storage
     */
    public TraceStorage getTraceStorage() {
        return traceStorage;
    }
    
    @Override
    public void afterPropertiesSet() {
    }
    
    private static RocketDestination createDestination(String topic) {
        RocketDestination rd = null;
        if (StringUtils.hasText(topic)) {
            rd = new RocketDestination(topic);
        }
        return rd;
    }
    
    private Location createLocation(SocketAddress bornHost) {
        if (bornHost == null) {
            return null;
        }
        
        Location location = new Location();
        location.setPid(NetUtil.getProcessId());
        location.setHost(bornHost.toString());
        location.setTraceId(traceStorage.getTraceId());
        location.setSpanId(traceStorage.getSpanId());
        location.setThreadId(Thread.currentThread().getId());
        location.setThreadName(Thread.currentThread().getName());
        
        return location;
    }
    
    private String getNameSrvAddrFromProperties() {
        String nameserverAddrs = this.properties.getProperty(PropertyKeyConst.NAMESRV_ADDR);
        if (StringUtils.hasText(nameserverAddrs)
                && NameAddrUtils.NAMESRV_ENDPOINT_PATTERN.matcher(nameserverAddrs.trim()).matches()) {
            return nameserverAddrs.substring(NameAddrUtils.ENDPOINT_PREFIX.length());
        }
        
        return nameserverAddrs;
    }
    
    public void refresh(RocketConsumerProperties input) {
        logger.info("refresh rocketmq consumer properties.");
        this.consumerProperties = input;
        this.properties.putAll(ONSUtil.extractProperties(input.toProperties()));
        boolean isStarted = this.isStarted();
        this.shutdown();

        this.nameServerAddr = getNameSrvAddrFromProperties();
        this.namespace = getNamespace();

        updateCredential(properties);

        this.pullConsumer = createConsumer();
        this.pushConsumer = createPushConsumer();

        for(RocketDestination rd : CONSUMERS.keySet()) {
            try {
                pushConsumer.subscribe(rd.getTopic(), rd.getTagsString());
            } catch (MQClientException e) {
                throw new RuntimeException(e);
            }
        }

        if(isStarted) {
            this.start();
        }
    }
    
    public void start() {
        logger.info("Starting RocketMQ consumer");
        if (started.compareAndSet(false, true)) {
            super.start();
            try {
                this.pullConsumer.start();
                this.pushConsumer.registerMessageListener(new PushMessageListener());
                this.pushConsumer.start();
            } catch (MQClientException e) {
                throw new RuntimeException(e);
            }
            this.started.set(true);
        }
    }
    
    public void shutdown() {
        if (started.compareAndSet(true, false)) {
            super.shutdown();
            try {
                this.pullConsumer.shutdown();
                this.pushConsumer.shutdown();
            } catch (Exception e) {
                logger.error("shutdown consumer error", e);
            }
        }
    }
    
    @Override
    protected void updateNameServerAddr(String newAddrs) {
        logger.info("update name server addr: {}", newAddrs);
        this.nameServerAddr = newAddrs;
        pushConsumer.setNamesrvAddr(newAddrs);
        pushConsumer.getDefaultMQPushConsumerImpl().getmQClientFactory()
                    .getMQClientAPIImpl().updateNameServerAddressList(newAddrs);
        pullConsumer.setNamesrvAddr(newAddrs);
        try {
            Field f = DefaultMQPullConsumerImpl.class
                    .getDeclaredField("mQClientFactory");
            f.setAccessible(true);
            MQClientInstance instance = (MQClientInstance) f.get(pullConsumer
                    .getDefaultMQPullConsumerImpl());
            instance.getMQClientAPIImpl().updateNameServerAddressList(newAddrs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    class PushMessageListener implements MessageListenerConcurrently {
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(
                List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
            if (msgs == null) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            for (MessageExt msg : msgs) {
                RocketDestination destination = createDestination(msg.getTopic());
                destination.addTag(msg.getTags());
                RocketDestination rd = CONSUMERS.keySet().stream().filter(
                        d -> d.isContains(destination)
                ).findFirst().orElse(null);
                if (rd == null) {
                    logger.error("can not find destination for message: {}", msg);
                    throw new CloudAppException("can not find destination for message: " + msg);
                }
                Notifier<MessageExt> notifier = CONSUMERS.get(rd);
                notifier.onMessageNotified(convertMessage(msg, rd));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }
    
}
