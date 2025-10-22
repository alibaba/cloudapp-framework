package com.alibaba.cloudapp.messaging.rocketmq;

import com.alibaba.ons.open.trace.core.common.OnsTraceConstants;
import com.alibaba.ons.open.trace.core.common.OnsTraceDispatcherType;
import com.alibaba.ons.open.trace.core.dispatch.NameServerAddressSetter;
import com.alibaba.ons.open.trace.core.dispatch.impl.AsyncArrayDispatcher;
import com.aliyun.openservices.ons.api.impl.authority.SessionCredentials;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.impl.consumer.DefaultMQPushConsumerImpl;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.impl.producer.DefaultMQProducerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class TracerUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(
            TracerUtil.class);
    
    public static AsyncArrayDispatcher createTraceDispatcher(
            SessionCredentials sessionCredentials,
            NameServerAddressSetter setter,
            DefaultMQPushConsumerImpl hostConsumer
    ) {
        Properties tempProperties = initProperties(sessionCredentials);
        tempProperties.put(OnsTraceConstants.InstanceName,
                           "PID_CLIENT_INNER_TRACE_CONSUMER"
        );
        tempProperties.put(OnsTraceConstants.TraceDispatcherType,
                           OnsTraceDispatcherType.CONSUMER.name()
        );
        try {
            AsyncArrayDispatcher dispatcher = new AsyncArrayDispatcher(
                    tempProperties, sessionCredentials,
                    setter
            );
            dispatcher.setHostConsumer(hostConsumer);
            return dispatcher;
        } catch (Throwable e) {
            logger.error("system mqtrace hook init failed,maybe can't send " +
                                 "msg trace data", e);
            return null;
        }
    }
    
    private static Properties initProperties(SessionCredentials sessionCredentials) {
        Properties tempProperties = new Properties();
        tempProperties.put(OnsTraceConstants.AccessKey,
                           sessionCredentials.getAccessKey()
        );
        tempProperties.put(OnsTraceConstants.SecretKey,
                           sessionCredentials.getSecretKey()
        );
        tempProperties.put(OnsTraceConstants.MaxMsgSize, "128000");
        tempProperties.put(OnsTraceConstants.AsyncBufferSize, "2048");
        tempProperties.put(OnsTraceConstants.MaxBatchNum, "100");
        return tempProperties;
    }
    
    public static AsyncArrayDispatcher createTraceDispatcher(
            SessionCredentials sessionCredentials,
            NameServerAddressSetter setter,
            DefaultMQProducerImpl hostProducer
    ) {
        Properties tempProperties = initProperties(sessionCredentials);
        tempProperties.put(OnsTraceConstants.InstanceName,
                           "PID_CLIENT_INNER_TRACE_PRODUCER"
        );
        tempProperties.put(
                OnsTraceConstants.TraceDispatcherType,
                OnsTraceDispatcherType.PRODUCER.name()
        );
        try {
            AsyncArrayDispatcher dispatcher = new AsyncArrayDispatcher(
                    tempProperties, sessionCredentials,
                    setter
            );
            dispatcher.setHostProducer(hostProducer);
            return dispatcher;
        } catch (Throwable e) {
            logger.error("system mqtrace hook init failed,maybe can't send " +
                                 "msg trace data", e);
            return null;
        }
    }
    
}
