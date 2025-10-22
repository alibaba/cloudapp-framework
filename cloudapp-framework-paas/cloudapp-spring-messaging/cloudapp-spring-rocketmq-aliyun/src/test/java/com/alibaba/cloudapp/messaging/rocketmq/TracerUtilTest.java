package com.alibaba.cloudapp.messaging.rocketmq;

import com.alibaba.ons.open.trace.core.dispatch.NameServerAddressSetter;
import com.alibaba.ons.open.trace.core.dispatch.impl.AsyncArrayDispatcher;
import com.aliyun.openservices.ons.api.impl.authority.SessionCredentials;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.impl.consumer.DefaultMQPushConsumerImpl;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.impl.producer.DefaultMQProducerImpl;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import org.junit.Test;

public class TracerUtilTest {
    
    @Test
    public void testCreateTraceDispatcher1() {
        // Setup
        final SessionCredentials sessionCredentials = new SessionCredentials();
        sessionCredentials.setAccessKey("accessKey");
        sessionCredentials.setSecretKey("secretKey");
        sessionCredentials.setSignature("signature");
        sessionCredentials.setSecurityToken("securityToken");
        sessionCredentials.setSignatureMethod("signatureMethod");
        
        final NameServerAddressSetter setter = null;
        final DefaultMQPushConsumerImpl hostConsumer = new DefaultMQPushConsumerImpl(
                new DefaultMQPushConsumer("namespace", "consumerGroup"), null);
        
        // Run the test
        final AsyncArrayDispatcher result = TracerUtil.createTraceDispatcher(
                sessionCredentials, setter, hostConsumer);
        
        // Verify the results
    }
    
    @Test
    public void testCreateTraceDispatcher2() {
        // Setup
        final SessionCredentials sessionCredentials = new SessionCredentials();
        sessionCredentials.setAccessKey("accessKey");
        sessionCredentials.setSecretKey("secretKey");
        sessionCredentials.setSignature("signature");
        sessionCredentials.setSecurityToken("securityToken");
        sessionCredentials.setSignatureMethod("signatureMethod");
        
        final NameServerAddressSetter setter = null;
        final DefaultMQProducerImpl hostProducer = new DefaultMQProducerImpl(
                new DefaultMQProducer("namespace", "producerGroup"));
        
        // Run the test
        final AsyncArrayDispatcher result = TracerUtil.createTraceDispatcher(
                sessionCredentials, setter, hostProducer);
        
        // Verify the results
    }
    
}
