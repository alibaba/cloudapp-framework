package com.alibaba.cloudapp.messaging.rocketmq.demo.service;

import com.alibaba.fastjson2.JSON;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.cloudapp.messaging.rocketmq.CloudAppRocketConsumer;
import com.alibaba.cloudapp.messaging.rocketmq.CloudAppRocketProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

@Service
public class RocketDemoService {
    
    private static final Logger logger = LoggerFactory.getLogger(RocketDemoService.class);
    
    @Autowired
    private CloudAppRocketConsumer cloudAppRocketConsumer;
    @Autowired
    private CloudAppRocketProducer producer;
    
    private static final String TOPIC = "test-topic";
    private static final String TOPIC2 = "demo-topic";
    
    public void pullMsg() {
        try {
            logger.info("pull msg from topic: {}", TOPIC);
            cloudAppRocketConsumer.pull(TOPIC, 10).forEach(
                    message -> logger.info("receive message: {}", JSON.toJSONString(message)));
        } catch (Throwable e) {
            logger.error("Pull mq message failed. Topic is: {}", TOPIC, e);
        }
    }
    public void sendData() {
        try {
            producer.send(TOPIC,"Hello MQ");
            producer.send(TOPIC2,"Hello MQ");
        } catch (Throwable e) {
            logger.error("Send mq message failed. Topic is: {}", TOPIC, e);
        }
    }
    
    @PostConstruct
    public void init() {
        cloudAppRocketConsumer.subscribe("demo-topic", message -> {
            logger.info("receive demo-topic message: {}",
                        JSON.toJSONString(message));
        });
        cloudAppRocketConsumer.start();
        producer.start();
    }
    
    public Object[] sendAsync() {
        try {
            CompletableFuture<SendResult> future = producer.sendAsync(
                    TOPIC, "Hello MQ");
            CompletableFuture<SendResult> future2 = producer.sendAsync(
                    TOPIC2, "Hello MQ");
            future.thenApply(result -> {
                logger.info("send message {}!", JSON.toJSONString(result));
                return result;
            });
            future2.thenApply(result -> {
                logger.info("send message {}!", JSON.toJSONString(result));
                return result;
            });
            return new Object[]{
                    future.get(),
                    future2.get()
                };
        } catch (Throwable e) {
            logger.error("Send mq message failed. Topic is: {}", TOPIC, e);
        }
        return new Object[0];
    }
    
}