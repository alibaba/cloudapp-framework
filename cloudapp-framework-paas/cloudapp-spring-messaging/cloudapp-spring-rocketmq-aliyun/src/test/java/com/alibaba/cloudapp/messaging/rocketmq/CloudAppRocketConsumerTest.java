package com.alibaba.cloudapp.messaging.rocketmq;


import com.aliyun.openservices.ons.api.impl.rocketmq.ONSChannel;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.*;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.store.RemoteBrokerOffsetStore;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.exception.MQClientException;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageQueue;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.alibaba.cloudapp.api.messaging.Notifier;
import com.alibaba.cloudapp.api.messaging.TraceStorage;
import com.alibaba.cloudapp.api.messaging.model.Destination;
import com.alibaba.cloudapp.api.messaging.model.MQMessage;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.messaging.rocketmq.model.RocketDestination;
import com.alibaba.cloudapp.messaging.rocketmq.properties.RocketConsumerProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CloudAppRocketConsumerTest {
    
    private final RocketConsumerProperties properties = new RocketConsumerProperties();
    
    @Mock
    private DefaultMQPullConsumer pullConsumer;
    @Mock
    private DefaultMQPushConsumer pushConsumer;
    
    @Mock
    private TraceStorage mockTraceStorage;
    
    @Mock
    private Notifier<MessageExt> mockNotifier;
    
    private MessageExt messageExt;
    
    private Destination destination;
    
    @Mock
    private CloudAppRocketConsumer consumer;
    
    private static final String TOPIC = "test-topic";
    
    @Before
    public void setUp() throws Exception {
        properties.setEnableMsgTrace(true);
        properties.setGroup("test-group");
        properties.setMessageModel(MessageModel.CLUSTERING.name());
        properties.setUseTLS(false);
        properties.setUsername("test-username");
        properties.setTopic(TOPIC);
        properties.setNameServer("test-name-server");
        properties.setPassword("test-password");
        properties.setTraceTopic("test-trace-topic");
        properties.setThreadNum(1);
        properties.setSuspendTimeMillis(1000);
        properties.setMaxTimeout(1000);
        properties.setPullBatchSize(1);
        properties.setName("test-name");
        properties.setAccessChannel(ONSChannel.ALIYUN.name());
        properties.setNamespace("test-namespace");
        properties.setTags(Collections.singletonList("test-tag"));
        
        destination = new RocketDestination(TOPIC);
        
        messageExt = new MessageExt(
                0, 0L,
                new InetSocketAddress("127.0.0.1", 8080),
                0L, new InetSocketAddress("127.0.0.1", 8080),
                "test-msg-id"
        );
        messageExt.setBody("test-body".getBytes());
        messageExt.setTopic(TOPIC);
        
        PullResult pullResult = new PullResult(
                PullStatus.FOUND, 1, 0, 0,
                Collections.singletonList(messageExt)
        );
        
        
        consumer = new CloudAppRocketConsumer(properties);
        consumer.setTraceStorage(mockTraceStorage);
        
        Field field = CloudAppRocketConsumer.class.getDeclaredField(
                "pullConsumer");
        field.setAccessible(true);
        field.set(consumer, pullConsumer);
        
        field = CloudAppRocketConsumer.class.getDeclaredField(
                "pushConsumer");
        field.setAccessible(true);
        field.set(consumer, pushConsumer);
        
        doReturn(pullResult).when(pullConsumer).pull(
                any(MessageQueue.class), anyString(), anyLong(), anyInt(), anyLong()
        );
        doNothing().when(pushConsumer).subscribe(
                anyString(), anyString()
        );
        
        MessageQueue mq = new MessageQueue(TOPIC, "brokerName", 0);
        doReturn(Collections.singleton(mq)).when(pullConsumer).fetchSubscribeMessageQueues(anyString());
        doNothing().when(pullConsumer).updateConsumeOffset(any(MessageQueue.class), anyLong());
        RemoteBrokerOffsetStore offsetStore =
                mock(RemoteBrokerOffsetStore.class);
        doReturn(offsetStore).when(pullConsumer).getOffsetStore();
        doNothing().when(offsetStore).updateConsumeOffsetToBroker(any(MessageQueue.class), anyLong(), anyBoolean());
    }
    
    @Test
    public void testGetDelegatingConsumer() {
        MQPullConsumer result = consumer.getDelegatingConsumer();
        assertEquals(pullConsumer, result);
    }
    
    @Test
    public void testPull1() {
        MQMessage<? extends MessageExt> result = consumer.pull(
                destination);
        
        assertEquals(messageExt, result.getMessageBody());
    }
    
    @Test
    public void testPull2() throws CloudAppException {
        Collection<MQMessage<? extends MessageExt>> result = consumer.pull(
                destination, 1);
        
        assertEquals(1, result.size());
        assertEquals(messageExt, result.iterator().next().getMessageBody());
    }
    
    @Test
    public void testPull3() {
        MessageExt result = consumer.pull(TOPIC);
        
        assertEquals(messageExt, result);
    }
    
    @Test
    public void testPull4() {
        Collection<MessageExt> result = consumer.pull(TOPIC, 1);
        
        assertEquals(1, result.size());
        assertEquals(messageExt, result.iterator().next());
    }
    
    @Test
    public void testPull5() throws CloudAppException {
        MQMessage<? extends MessageExt> result = consumer.pull(
                destination, Duration.ofSeconds(5));
        
        assertEquals(messageExt, result.getMessageBody());
    }
    
    @Test
    public void testPull6() {
        MessageExt result = consumer.pull(TOPIC, Duration.ofSeconds(5));
        
        assertEquals(messageExt, result);
    }
    
    @Test
    public void testPull7() throws CloudAppException {
        Collection<MQMessage<? extends MessageExt>> result = consumer.pull(
                destination, 1, Duration.ofSeconds(5));
        
        assertEquals(1, result.size());
        assertEquals(messageExt, result.iterator().next().getMessageBody());
    }
    
    @Test
    public void testPull8() {
        Collection<MessageExt> result = consumer.pull(
                TOPIC, 1,  Duration.ofSeconds(5)
        );
        
        assertEquals(1, result.size());
        assertEquals(messageExt, result.iterator().next());
    }
    
    @Test
    public void testSubscribe1() {
        consumer.subscribe(destination, mockNotifier);
    }
    
    @Test
    public void testUnsubscribe1() {
        RocketDestination destination = new RocketDestination("demo");
        consumer.subscribe(destination, mockNotifier);
        consumer.unsubscribe(destination);
    }
    
    @Test
    public void testUnsubscribe2() {
        RocketDestination destination = new RocketDestination("demo");
        consumer.subscribe(destination, mockNotifier);
        consumer.unsubscribe("demo");
    }
    
    @Test
    public void testConvertMessage() {
        MQMessage<MessageExt> result = consumer.convertMessage(
                messageExt, destination
        );
        
        assertEquals(messageExt, result.getMessageBody());
        assertEquals(destination, result.getDestination());
    }
    
    @Test
    public void refresh() throws MQClientException {
        consumer.refresh(properties);
        assert consumer.getDelegatingConsumer() != null;
    }
    
    @Test
    public void afterPropertiesSet() {
        consumer.afterPropertiesSet();
        assert consumer.getDelegatingConsumer() != null;
    }
    
    @Test
    public void convertMessage() {
        MQMessage<MessageExt> result = consumer.convertMessage(
                messageExt, destination);
        
        assertEquals(messageExt, result.getMessageBody());
        assertEquals(destination, result.getDestination());
    }
    
}
