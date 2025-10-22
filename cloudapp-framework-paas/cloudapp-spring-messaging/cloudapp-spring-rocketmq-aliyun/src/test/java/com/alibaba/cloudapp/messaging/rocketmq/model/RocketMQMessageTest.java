package com.alibaba.cloudapp.messaging.rocketmq.model;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.cloudapp.api.messaging.model.Destination;
import com.alibaba.cloudapp.api.messaging.model.Location;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RocketMQMessageTest {
    
    private RocketMQMessage rocketMQMessageUnderTest;
    
    @Before
    public void setUp() {
        rocketMQMessageUnderTest = new RocketMQMessage();
    }
    
    @Test
    public void testMessageIDGetterAndSetter() {
        final String messageID = "messageID";
        rocketMQMessageUnderTest.setMessageID(messageID);
        assertEquals(messageID, rocketMQMessageUnderTest.getMessageID());
    }
    
    @Test
    public void testMessageBodyGetterAndSetter() {
        final MessageExt messageBody = new MessageExt();
        rocketMQMessageUnderTest.setMessageBody(messageBody);
        assertEquals(messageBody,
                     rocketMQMessageUnderTest.getUnderlyingMessage()
        );
    }
    
    @Test
    public void testDestinationGetterAndSetter() {
        final Destination destination = null;
        rocketMQMessageUnderTest.setDestination(destination);
        assertEquals(destination, rocketMQMessageUnderTest.getDestination());
    }
    
    @Test
    public void testReplyToGetterAndSetter() {
        final Destination replyTo = null;
        rocketMQMessageUnderTest.setReplyTo(replyTo);
        assertEquals(replyTo, rocketMQMessageUnderTest.getReplyTo());
    }
    
    @Test
    public void testSentTimestampGetterAndSetter() {
        final long sentTimestamp = 0L;
        rocketMQMessageUnderTest.setSentTimestamp(sentTimestamp);
        assertEquals(sentTimestamp,
                     rocketMQMessageUnderTest.getSentTimestamp()
        );
    }
    
    @Test
    public void testDeliveredTimestampGetterAndSetter() {
        final long deliveredTimestamp = 0L;
        rocketMQMessageUnderTest.setDeliveredTimestamp(deliveredTimestamp);
        assertEquals(deliveredTimestamp,
                     rocketMQMessageUnderTest.getDeliveredTimestamp()
        );
    }
    
    @Test
    public void testReceivedTimestampGetterAndSetter() {
        final long receivedTimestamp = 0L;
        rocketMQMessageUnderTest.setReceivedTimestamp(receivedTimestamp);
        assertEquals(receivedTimestamp,
                     rocketMQMessageUnderTest.getReceivedTimestamp()
        );
    }
    
    @Test
    public void testMessageBody1GetterAndSetter() {
        final MessageExt messageBody = new MessageExt();
        rocketMQMessageUnderTest.setMessageBody(messageBody);
        assertEquals(messageBody, rocketMQMessageUnderTest.getMessageBody());
    }
    
    @Test
    public void testSenderGetterAndSetter() {
        final Location sender = new Location();
        rocketMQMessageUnderTest.setSender(sender);
        assertEquals(sender, rocketMQMessageUnderTest.getSender());
    }
    
    @Test
    public void testReceiverGetterAndSetter() {
        final Location receiver = new Location();
        rocketMQMessageUnderTest.setReceiver(receiver);
        assertEquals(receiver, rocketMQMessageUnderTest.getReceiver());
    }
    
}
