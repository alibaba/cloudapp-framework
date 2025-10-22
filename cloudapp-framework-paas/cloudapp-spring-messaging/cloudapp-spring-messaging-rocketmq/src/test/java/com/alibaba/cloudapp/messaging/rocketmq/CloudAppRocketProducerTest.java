package com.alibaba.cloudapp.messaging.rocketmq;


import com.alibaba.cloudapp.api.messaging.model.MQMessage;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.messaging.rocketmq.model.RocketDestination;
import com.alibaba.cloudapp.messaging.rocketmq.properties.RocketProducerProperties;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageClientExt;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CloudAppRocketProducerTest {

    @Mock
    private RocketProducerProperties mockProperties;
    @Mock
    private RocketDestination destination;
    @Mock
    private MQMessage<? extends MessageExt> message;
    @Mock
    private MessageClientExt messageExt;
    @Mock
    private DefaultMQProducer mockProducer;

    @InjectMocks
    private CloudAppRocketProducer producer;

    @Before
    public void setUp() {
        when(destination.getTopic()).thenReturn("topic");
        when(destination.getTagsString()).thenReturn("*");

        when(message.getDestination()).thenReturn(destination);
        doReturn(messageExt).when(message).getMessageBody();

        producer = new CloudAppRocketProducer(mockProperties){
            @Override
            public DefaultMQProducer createProducer() {
                return mockProducer;
            }
        };
    }

    @Test
    public void testGetDelegatingProducer() {
        MQProducer result = producer.getDelegatingProducer();
        assertSame(mockProducer, result);
    }

    @Test
    public void testSend1() throws Exception {
        producer.send(message);
        verify(mockProducer).send(messageExt);
    }

    @Test
    public void testSend1_ThrowsCloudAppException() {
        try {
            doThrow(new CloudAppException("Test Exception"))
                    .when(mockProducer).send(messageExt);
            producer.send(message);
        } catch (Exception e) {
            assertEquals("Test Exception", e.getCause().getMessage());
        }
    }

    @Test
    public void testSend2() throws Exception {
        MessageExt messageExt = new MessageExt();
        messageExt.setTopic("topic");
        messageExt.setBrokerName("brokerName");
        messageExt.setQueueId(0);
        messageExt.setBornTimestamp(0L);
        messageExt.setBornHost(new InetSocketAddress("localhost", 80));

        producer.send(destination, messageExt);
        verify(mockProducer).send(messageExt);
    }

    @Test
    public void testSend2_ThrowsCloudAppException() {
        try {
            MessageExt messageExt = new MessageExt();
            messageExt.setTopic("topic");
            messageExt.setBrokerName("brokerName");
            messageExt.setQueueId(0);
            messageExt.setBornTimestamp(0L);
            messageExt.setBornHost(new InetSocketAddress("localhost", 80));

            doThrow(new CloudAppException("Test Exception"))
                    .when(mockProducer).send(messageExt);
            producer.send(destination, messageExt);
        } catch (Exception e) {
            assertEquals("Test Exception", e.getCause().getMessage());
        }
    }
    
    @Test
    public void testSend_ThrowsCloudAppException() {
        try {
            doThrow(new CloudAppException("Test Exception"))
                    .when(mockProducer).send(any(Message.class));
            producer.send(destination, "messageExt");
        } catch (Exception e) {
            assertEquals("Test Exception", e.getCause().getMessage());
        }
    }

    @Test
    public void testSend3() throws Exception {
        producer.send("topic", "message");
        verify(mockProducer).send(any(Message.class));
    }

    @Test
    public void testSend3_ThrowsCloudAppException()  {
        try {
            doThrow(new CloudAppException("Test Exception"))
                    .when(mockProducer).send(any(Message.class));
            producer.send("topic", "message");
        } catch (Exception e) {
            assertEquals("Test Exception", e.getCause().getMessage());
        }
    }

    @Test
    public void testSend4() throws Exception {
        producer.send(destination, "message");
        verify(mockProducer).send(any(Message.class));
    }

    @Test
    public void testSendAsync1() throws Exception {
        CompletableFuture<SendResult> future = producer.sendAsync(message);
        verify(mockProducer).send(any(Message.class), any(SendCallback.class));
    }

    @Test
    public void testSendAsync2() throws Exception {
        MessageExt messageExt = new MessageExt();
        messageExt.setTopic("topic");
        messageExt.setBrokerName("brokerName");
        messageExt.setQueueId(0);
        messageExt.setBornTimestamp(0L);
        messageExt.setBornHost(new InetSocketAddress("localhost", 80));

        CompletableFuture<SendResult> future = producer.sendAsync(destination, messageExt);
        verify(mockProducer).send(any(Message.class), any(SendCallback.class));
    }

    @Test
    public void testSendAsync3() throws Exception {
        doAnswer(r -> {
            ((SendCallback) r.getArgument(1)).onSuccess(null);
            return r;
        }).when(mockProducer).send(any(Message.class), any(SendCallback.class));
        
        CompletableFuture<SendResult> future = producer.
                sendAsync("topic", "message");
        
        verify(mockProducer).send(any(Message.class), any(SendCallback.class));
    }
    
    @Test
    public void testSendAsync3_throwsException() throws Exception {
        doAnswer(r -> {
            ((SendCallback) r.getArgument(1)).onException(null);
            throw new Exception();
        }).when(mockProducer).send(any(Message.class), any(SendCallback.class));
        
        assertThrows(CloudAppException.class,
                     () -> producer.sendAsync("topic", "message")
        );
    }

    @Test
    public void testSendAsync4() throws Exception {
        CompletableFuture<SendResult> future = producer.sendAsync(destination, "message");
        verify(mockProducer).send(any(Message.class), any(SendCallback.class));
    }

    @Test
    public void testClose() {
        producer.close();
        verify(mockProducer).shutdown();
    }

    @Test
    public void testRefresh() throws MQClientException {
        RocketProducerProperties newProperties = mock(RocketProducerProperties.class);
        producer.setStarted(true);
        producer.refresh(newProperties);
        verify(mockProducer).shutdown();
        verify(mockProducer).start();
    }
    
    @Test
    public void createProducer() {
        producer = new CloudAppRocketProducer(mockProperties);
        
        DefaultMQProducer pr = producer.createProducer();
        assertNotNull(pr);
        
    }
}
