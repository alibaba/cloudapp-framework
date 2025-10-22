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

package com.alibaba.cloudapp.messaging.kafka.plaintext.demo.controller;

import com.alibaba.cloudapp.api.messaging.Producer;
import com.alibaba.cloudapp.api.messaging.model.MQMessage;
import com.alibaba.cloudapp.messaging.kafka.CloudAppKafkaProducer;
import com.alibaba.cloudapp.messaging.kafka.CloudAppKafkaProducerFactory;
import com.alibaba.cloudapp.messaging.kafka.properties.KafkaProducerProperties;
import com.alibaba.cloudapp.util.DateUtil;
import com.alibaba.cloudapp.util.RandomStringGenerator;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class KafkaProducerDemoController {

    static ThreadLocal<Producer> threadLocal = new ThreadLocal<>();

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerDemoController.class);
    
    @Autowired
    @Qualifier("testKafkaProducerConfig")
    KafkaProducerProperties kafkaProducerProperties;

    @Autowired
    @Qualifier("testKafkaProducerTemplate")
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    @Qualifier("testKafkaProducerFactory")
    CloudAppKafkaProducerFactory producerFactory;


    @RequestMapping("/sendSimpleMQ")
    public void sendSimpleMQ() {
        kafkaProducerProperties.setName("test-producer" + RandomStringGenerator.generate(3));
        CloudAppKafkaProducer producer = new CloudAppKafkaProducer(kafkaProducerProperties);
        producer.send("test-topic", "hello world!" + DateUtil.getCurrentDateTimeString());
    }

    @RequestMapping("/sendAsyncSimpleMQ")
    public void asynSendSimpleMQ() {
        kafkaProducerProperties.setName("test-producer" + RandomStringGenerator.generate(3));
        CloudAppKafkaProducer producer = new CloudAppKafkaProducer(kafkaProducerProperties);
        producer.sendAsync("test-topic", "hello world!" + DateUtil.getCurrentDateTimeString());
    }

    @RequestMapping("/sendNativeMQ")
    public void sendNativeMQ() {
        kafkaProducerProperties.setName("test-producer" + RandomStringGenerator.generate(3));
        CloudAppKafkaProducer producer = new CloudAppKafkaProducer(kafkaProducerProperties);
        ProducerRecord record = new ProducerRecord<>("test-topic", "messageKey", "hello world!" + DateUtil.getCurrentDateTimeString());
        producer.send(record);
    }

    @RequestMapping("/sendWrapMQ")
    public void sendWrapMQ() {
        kafkaProducerProperties.setName("test-producer" + RandomStringGenerator.generate(3));
        CloudAppKafkaProducer producer = new CloudAppKafkaProducer(kafkaProducerProperties);
        ProducerRecord record = new ProducerRecord<>("test-topic", "messageKey", "hello world!" + DateUtil.getCurrentDateTimeString());
        MQMessage mqMessage = new MQMessage<ProducerRecord>() {
        };
        mqMessage.setMessageBody(record);
        producer.send(mqMessage);
    }

    @RequestMapping("/sendByKafkaProducerProperties")
    public void sendByKafkaProducerProperties() {
        kafkaProducerProperties.getProperties().forEach((k, v) -> logger.info("key:{}, value:{}", k, v));
        kafkaProducerProperties.setName("test-producer" + RandomStringGenerator.generate(3));
        CloudAppKafkaProducer producer = new CloudAppKafkaProducer(kafkaProducerProperties);
        producer.send("test-topic", "hello world!");
    }

    @RequestMapping("/sendByKafkaTemplate")
    public void sendByKafkaTemplate() {
        kafkaTemplate.sendDefault("Hello， 123123123");
        kafkaTemplate.send("test-topic", "hello world!");
    }

    @RequestMapping("/sendByCloudAppKafkaProducer")
    public void sendByCloudAppKafkaProducer() {
        CloudAppKafkaProducer producer = producerFactory.createRawProducer(
                kafkaProducerProperties.buildProducerProperties());

        producer.send("test-topic", "hello world!");
    }

    @RequestMapping("/sendByDelegateKafkaProducer")
    public void sendByDelegateKafkaProducer() {
        CloudAppKafkaProducer producer = producerFactory.createRawProducer(
                kafkaProducerProperties.buildProducerProperties());
        ProducerRecord<String, String> record = new ProducerRecord<>("test-topic", "messageKey", "hello world!");
        producer.getDelegatingProducer().send(record);
    }
}
