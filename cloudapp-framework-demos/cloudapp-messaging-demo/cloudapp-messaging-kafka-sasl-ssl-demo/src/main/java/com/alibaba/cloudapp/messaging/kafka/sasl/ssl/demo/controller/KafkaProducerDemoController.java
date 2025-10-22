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

package com.alibaba.cloudapp.messaging.kafka.sasl.ssl.demo.controller;

import com.alibaba.cloudapp.messaging.kafka.CloudAppKafkaProducer;
import com.alibaba.cloudapp.messaging.kafka.properties.KafkaProducerProperties;
import com.alibaba.cloudapp.util.DateUtil;
import com.alibaba.cloudapp.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducerDemoController {

    @Autowired
    @Qualifier("testKafkaProducerConfig")
    KafkaProducerProperties kafkaProducerProperties;

    @RequestMapping("/sendSimpleMQ")
    public void sendSimpleMQ() {
        kafkaProducerProperties.setName("test-producer" + RandomStringGenerator.generate(3));
        CloudAppKafkaProducer producer = new CloudAppKafkaProducer(kafkaProducerProperties);
        producer.send("test-topic", "hello world!" + DateUtil.getCurrentDateTimeString());
    }

}