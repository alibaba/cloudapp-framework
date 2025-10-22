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

package com.alibaba.cloudapp.messaging.kafka.sasl.plaintext.demo.controller;

import com.alibaba.cloudapp.messaging.kafka.CloudAppKafkaConsumer;
import com.alibaba.cloudapp.messaging.kafka.properties.KafkaConsumerProperties;
import com.alibaba.cloudapp.util.RandomStringGenerator;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class KafkaConsumerDemoController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerDemoController.class);

    @Autowired
    @Qualifier("testKafkaConsumerConfig")
    KafkaConsumerProperties kafkaConsumerProperties;

    @RequestMapping("/poll")
    public ConsumerRecords poll() {
        kafkaConsumerProperties.setName("test-consumer" + RandomStringGenerator.generate(3));
        CloudAppKafkaConsumer consumer = new CloudAppKafkaConsumer(kafkaConsumerProperties);
        consumer.subscribe(Collections.singletonList("test-topic"));
        ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : consumerRecords) {
            logger.info("received message: key = {}, value = {}, offset = {}.", record.key(), record.value(), record.offset());
        }
        return consumerRecords;
    }

}