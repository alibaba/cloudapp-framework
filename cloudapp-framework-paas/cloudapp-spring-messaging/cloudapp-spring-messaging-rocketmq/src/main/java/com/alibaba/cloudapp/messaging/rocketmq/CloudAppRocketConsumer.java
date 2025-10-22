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
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.*;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.CommunicationMode;
import org.apache.rocketmq.client.impl.consumer.DefaultLitePullConsumerImpl;
import org.apache.rocketmq.client.impl.consumer.DefaultMQPushConsumerImpl;
import org.apache.rocketmq.client.impl.factory.MQClientInstance;
import org.apache.rocketmq.client.trace.AsyncTraceDispatcher;
import org.apache.rocketmq.client.trace.TraceDispatcher;
import org.apache.rocketmq.client.trace.hook.ConsumeMessageTraceHookImpl;
import org.apache.rocketmq.common.filter.ExpressionType;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.sysflag.PullSysFlag;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class CloudAppRocketConsumer implements
        InitializingBean, Consumer<LitePullConsumer, MessageExt> {
    
    private static final int SINGLE = 1;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);
    
    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppRocketConsumer.class);
    
    private RocketConsumerProperties consumerProperties;
    
    private TraceStorage traceStorage = new ThreadLocalTraceStorage();
    private String namespace;
    private SessionCredentials sessionCredentials;
    private String nameServerAddr;
    private TraceDispatcher traceDispatcher;
    private final AtomicBoolean started = new AtomicBoolean(false);
    
    private static final Map<RocketDestination, Notifier<MessageExt>> CONSUMERS =
            Collections.synchronizedMap(new HashMap<>());
    
    /**
     * default consumer and subscribed all topic
     */
    private DefaultLitePullConsumer pullConsumer;
    private DefaultMQPushConsumer pushConsumer;
    
    public CloudAppRocketConsumer(RocketConsumerProperties properties) {
        this.namespace = properties.getNamespace();
        this.consumerProperties = properties;
        this.pushConsumer = createPushConsumer();
        this.pullConsumer = createConsumer();
    }
    
    @Override
    public LitePullConsumer getDelegatingConsumer() {
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
    
    private DefaultLitePullConsumer createConsumer() {
        try {
            String name = "pull" + consumerProperties.getName()
                    + new Random().nextInt(10000);
            DefaultLitePullConsumer consumer = new DefaultLitePullConsumer(
                    consumerProperties.getGroup(),
                    new AclClientRPCHook(sessionCredentials)
            );
            consumer.setNamespaceV2(namespace);
            consumer.setNamesrvAddr(nameServerAddr);
            consumer.setConsumerGroup(consumerProperties.getGroup());
            consumer.setInstanceName(name);
            consumer.setVipChannelEnabled(consumerProperties.isVipChannelEnable());
            consumer.setConsumerTimeoutMillisWhenSuspend(consumerProperties.getSuspendTimeMillis());
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
                    consumerProperties.getGroup(),
                    new AclClientRPCHook(sessionCredentials)
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
            
            consumer.setNamespaceV2(namespace);
            consumer.setInstanceName(name);
            consumer.setNamesrvAddr(this.nameServerAddr);
            consumer.setVipChannelEnabled(consumerProperties.isVipChannelEnable());
            boolean msgTraceSwitch = consumerProperties.getEnableMsgTrace();
            if(msgTraceSwitch) {
                AsyncTraceDispatcher dispatcher = new AsyncTraceDispatcher(
                        consumerProperties.getGroup(),
                        TraceDispatcher.Type.CONSUME,
                        consumerProperties.getTraceTopic(),
                        null
                );
                dispatcher.getTraceProducer().setUseTLS(consumerProperties.isUseTLS());
                this.traceDispatcher = dispatcher;
                consumer.getDefaultMQPushConsumerImpl()
                        .registerConsumeMessageHook(
                                new ConsumeMessageTraceHookImpl(traceDispatcher)
                        );
            }
            
            consumer.setPostSubscriptionWhenPull(false);
            MessageModel messageModel = consumerProperties.getMessageModel();
            consumer.setMessageModel(messageModel);
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
            Collection<MessageQueue> set = pullConsumer.fetchMessageQueues(topic);
            List<MessageExt> list =
                    Collections.synchronizedList(new ArrayList<>(count));
            if(set.isEmpty()) {
                return list;
            }
            int pullCount = 0;
            for (MessageQueue queue : set) {
                try {
                    DefaultLitePullConsumerImpl impl = getDefaultLitePullConsumerImpl();
                    long offset = impl.searchOffset(queue, 0L);
                    int sysFlag = PullSysFlag.buildSysFlag(
                            false, true, true,
                            false, true);
                    
                    PullResult pullResult = impl
                            .getPullAPIWrapper()
                            .pullKernelImpl(
                                    queue,
                                    tag,
                                    ExpressionType.TAG,
                                    0L,
                                    offset,
                                    count,
                                    sysFlag,
                                    0,
                                    pullConsumer.getBrokerSuspendMaxTimeMillis(),
                                    timeout.toMillis(),
                                    CommunicationMode.SYNC,
                                    null
                    );
                    impl.updateConsumeOffset(queue, pullResult.getNextBeginOffset());
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
    
    private DefaultLitePullConsumerImpl getDefaultLitePullConsumerImpl() throws Exception{
        Field f = DefaultLitePullConsumer.class
                .getDeclaredField("defaultLitePullConsumerImpl");
        f.setAccessible(true);
        return (DefaultLitePullConsumerImpl) f.get(pullConsumer);
    }
    
    /**
     * setting trace storage
     * @param traceStorage trace storage
     */
    public void setTraceStorage(TraceStorage traceStorage) {
        this.traceStorage = traceStorage;
    }
    
    /**
     * get current trace storage
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
    
    public void refresh(RocketConsumerProperties input) {
        logger.info("refresh rocketmq consumer properties.");
        this.consumerProperties = input;
//        this.properties.putAll(ONSUtil.extractProperties(input.toProperties()));
        boolean isStarted = this.isStarted();
        this.shutdown();

        this.nameServerAddr = input.getNameServer();
        this.namespace = input.getNamespace();

        this.sessionCredentials = new SessionCredentials(
                input.getUsername(), input.getPassword()
        );

        this.pullConsumer = createConsumer();
        this.pushConsumer = createPushConsumer();
        this.traceDispatcher = null;

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
            try {
                this.pullConsumer.shutdown();
                this.pushConsumer.shutdown();
            } catch (Exception e) {
                logger.error("shutdown consumer error", e);
            }
        }
    }
    
    protected void updateNameServerAddr(String newAddrs) {
        logger.info("update name server addr: {}", newAddrs);
        this.nameServerAddr = newAddrs;
        pushConsumer.setNamesrvAddr(newAddrs);
        pullConsumer.setNamesrvAddr(newAddrs);
        try {
            Field field = DefaultLitePullConsumer.class
                    .getDeclaredField("defaultLitePullConsumerImpl");
            field.setAccessible(true);
            DefaultLitePullConsumerImpl impl = (DefaultLitePullConsumerImpl) field.get(pullConsumer);
            field = DefaultLitePullConsumerImpl.class
                    .getDeclaredField("mQClientFactory");
            field.setAccessible(true);
            
            MQClientInstance instance = (MQClientInstance) field.get(impl);
            instance.getMQClientAPIImpl().updateNameServerAddressList(newAddrs);
            
            field = DefaultMQPushConsumer.class.getDeclaredField(
                    "defaultMQPushConsumerImpl");
            field.setAccessible(true);
            DefaultMQPushConsumerImpl pushImpl = (DefaultMQPushConsumerImpl) field.get(pushConsumer);
            
            pushImpl.getmQClientFactory().getMQClientAPIImpl().updateNameServerAddressList(newAddrs);
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
    
    public String getNameServerAddr() {
        return nameServerAddr;
    }
    
    public void setNameServerAddr(String nameServerAddr) {
        this.nameServerAddr = nameServerAddr;
    }
    
    public boolean isStarted() {
        return started.get();
    }
    
    public void setStarted(boolean started) {
        this.started.set(started);
    }
}
