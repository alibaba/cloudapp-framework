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

package com.alibaba.cloudapp.starter.messaging.factory;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.MQPullConsumer;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.exception.MQClientException;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.producer.MQProducer;
import com.alibaba.cloudapp.api.messaging.model.ConsumerType;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.messaging.rocketmq.CloudAppRocketConsumer;
import com.alibaba.cloudapp.messaging.rocketmq.CloudAppRocketProducer;
import com.alibaba.cloudapp.messaging.rocketmq.properties.RocketConsumerProperties;
import com.alibaba.cloudapp.messaging.rocketmq.properties.RocketProducerProperties;
import com.alibaba.cloudapp.starter.messaging.properties.CloudAppRocketProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CloudAppRocketBeanFactory implements BeanFactoryPostProcessor,
        EnvironmentAware {
    
    private static final Logger logger = LoggerFactory.getLogger(
            CloudAppRocketBeanFactory.class);
    
    private CloudAppRocketProperties properties;
    private Environment environment;
    private ConfigurableListableBeanFactory beanFactory;
    
    private static final Map<String, CloudAppRocketProducer> producerCache =
            Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, CloudAppRocketConsumer> consumerCache =
            Collections.synchronizedMap(new HashMap<>());
    
    public CloudAppRocketBeanFactory() {}
    
    private void initServer() {
        
        if (properties.getOutputs() == null) {
            logger.warn("rocketmq output properties is empty");
            properties.setOutputs(Collections.emptyList());
        }
        if (properties.getInputs() == null) {
            logger.warn("rocketmq input properties is empty");
            properties.setInputs(Collections.emptyList());
        }
        
        if (environment == null) {
            return;
        }
        
        String serverKey = "rocketmq.name-server";
        if (!environment.containsProperty(serverKey)
                && StringUtils.hasText(this.properties.getNameServer())) {
            System.setProperty(serverKey, this.properties.getNameServer());
        }
        
        if (environment.containsProperty(serverKey)
                && !StringUtils.hasText(environment.getProperty(serverKey))
                && StringUtils.hasText(properties.getNameServer())
        ) {
            
            System.setProperty(serverKey, properties.getNameServer());
            System.setProperty(
                    "rocketmq.access-channel", properties.getAccessChannel()
            );
        }
    }
    
    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
        MQProducer defaultProducer = null;
        MQPullConsumer defaultConsumer = null;
        this.beanFactory = beanFactory;
        
        try {
            for (RocketProducerProperties output : properties.getOutputs()) {
                if (beanFactory.containsBean(output.getName())) {
                    throw new IllegalStateException(
                            "create rocketmq producer error, there has exists a bean named "
                                    + output.getName());
                }
                
                initProducer(output);
            }
            
            for (RocketConsumerProperties input : properties.getInputs()) {
                if (beanFactory.containsBean(input.getName())) {
                    throw new IllegalStateException(
                            "Create rocketmq consumer error, exists a bean named "
                                    + input.getName());
                }
                
                initConsumer(input);
            }
        } catch (Exception e) {
            throw new BeanCreationException("create rocketmq bean error", e);
        }
    }
    
    private void initConsumer(RocketConsumerProperties input) {
        
        if (ConsumerType.PUSH.toString().equals(input.getType())) {
            return;
        }
        
        initConsumerProperties(properties, input);
        CloudAppRocketConsumer consumer;
        
        if (consumerCache.containsKey(input.getName())) {
            consumer = consumerCache.get(input.getName());
            consumer.refresh(input);
            
        } else {
            consumer = new CloudAppRocketConsumer(input);
            beanFactory.registerSingleton(input.getName(), consumer);
            beanFactory.initializeBean(consumer, input.getName());
            
            consumerCache.put(input.getName(), consumer);
        }
    }
    
    private void initProducer(RocketProducerProperties output)
            throws MQClientException {
        CloudAppRocketProducer producer;
        
        initProducerProperties(properties, output);
        
        String name = output.getName();
        
        if (producerCache.containsKey(name)) {
            producer = producerCache.get(name);
            producer.refresh(output);
            
        } else {
            producer = new CloudAppRocketProducer(output);
            beanFactory.registerSingleton(output.getName(), producer);
            beanFactory.initializeBean(producer, output.getName());
            
            producerCache.put(name, producer);
        }
    }
    
    private void initProducerProperties(CloudAppRocketProperties properties,
                                        RocketProducerProperties output) {
        if (!StringUtils.hasText(output.getNameServer())) {
            output.setNameServer(properties.getNameServer());
            output.setUseTLS(properties.isUseTLS());
            
        }
        if (!StringUtils.hasText(output.getUsername())) {
            output.setUsername(properties.getUsername());
        }
        if (!StringUtils.hasText(output.getPassword())) {
            output.setPassword(properties.getPassword());
        }
        if (output.isEnableMsgTrace() == null) {
            output.setEnableMsgTrace(properties.isEnableMsgTrace());
            output.setTraceTopic(properties.getTraceTopic());
        }
    }
    
    private void initConsumerProperties(CloudAppRocketProperties properties,
                                        RocketConsumerProperties input) {
        if (!StringUtils.hasText(input.getNameServer())) {
            input.setNameServer(properties.getNameServer());
            input.setUseTLS(properties.isUseTLS());
        }
        if (!StringUtils.hasText(input.getUsername())) {
            input.setUsername(properties.getUsername());
        }
        if (!StringUtils.hasText(input.getPassword())) {
            input.setPassword(properties.getPassword());
        }
        if (input.isEnableMsgTrace() == null) {
            input.setEnableMsgTrace(properties.isEnableMsgTrace());
            input.setTraceTopic(properties.getTraceTopic());
        }
    }
    
    public void refresh(CloudAppRocketProperties properties) {
        if (properties == null || (properties.getNameServer() == null
                && properties.getInputs() == null && properties.getOutputs() == null)) {
            return;
        }
        
        if (environment == null) {
            return;
        }
        
        this.properties = properties;
        initServer();
        
        if (properties.getInputs() != null) {
            properties.getInputs().forEach(this::initConsumer);
        }
        
        if (properties.getOutputs() != null) {
            properties.getOutputs().forEach(output -> {
                try {
                    initProducer(output);
                } catch (MQClientException e) {
                    throw new CloudAppException("refresh producer error", e);
                }
            });
        }
    }
    
    @Override
    public void setEnvironment(@NotNull Environment environment)
            throws BeansException {
        
        this.environment = environment;
        
        if (properties == null || !StringUtils.hasText(properties.getNameServer())) {
            // init rocketmq config
            BindResult<CloudAppRocketProperties> result = Binder
                    .get(environment)
                    .bind(
                            CloudAppRocketProperties.PREFIX,
                            CloudAppRocketProperties.class
                    );
            
            if(this.properties == null) {
                properties = new CloudAppRocketProperties();
            }
            
            BeanUtils.copyProperties(result.get(), properties);
        }
        
        initServer();
    }
    
}
