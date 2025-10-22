package com.alibaba.cloudapp.starter.messaging.factory;

import com.alibaba.cloudapp.messaging.rocketmq.properties.RocketConsumerProperties;
import com.alibaba.cloudapp.messaging.rocketmq.properties.RocketProducerProperties;
import com.alibaba.cloudapp.starter.messaging.properties.CloudAppRocketProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.mock.env.MockEnvironment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CloudAppRocketBeanFactoryTest {
    
    private CloudAppRocketBeanFactory rocketBeanFactory;
    
    @Mock
    private ConfigurableListableBeanFactory beanFactory;
    private CloudAppRocketProperties properties;
    
    @BeforeEach
    void setUp() {
        properties = new CloudAppRocketProperties();
        RocketConsumerProperties consumerProperties = new RocketConsumerProperties();
        consumerProperties.setName("consumer");
        consumerProperties.setNameServer("nameServer");
        consumerProperties.setAccessChannel("accessChannel");
        
        properties.setInputs(Collections.singletonList(consumerProperties));
        
        RocketProducerProperties producerProperties = new RocketProducerProperties();
        producerProperties.setName("producer");
        producerProperties.setNameServer("nameServer");
        
        properties.setOutputs(Collections.singletonList(producerProperties));
        
        rocketBeanFactory = new CloudAppRocketBeanFactory();
    }
    
    @Test
    void testPostProcessBeanFactory() throws Exception {
        // Setup
        Field field = rocketBeanFactory.getClass().getDeclaredField("environment");
        field.setAccessible(true);
        field.set(rocketBeanFactory, new MockEnvironment());
        
        field = rocketBeanFactory.getClass().getDeclaredField("properties");
        field.setAccessible(true);
        field.set(rocketBeanFactory, properties);
        
        // Run the test
        rocketBeanFactory.postProcessBeanFactory(beanFactory);
        
        // Verify the results
    }
    
    @Test
    void testPostProcessBeanFactory_ThrowsBeansException() throws Exception {
        // Setup
        Field field = rocketBeanFactory.getClass().getDeclaredField("environment");
        field.setAccessible(true);
        field.set(rocketBeanFactory, new MockEnvironment());
        
        field = rocketBeanFactory.getClass().getDeclaredField("properties");
        field.setAccessible(true);
        field.set(rocketBeanFactory, properties);
        
        // Run the test
        assertThrows(BeansException.class,
                     () -> rocketBeanFactory.postProcessBeanFactory(
                             beanFactory)
        );
    }
    
    @Test
    void testRefresh() throws Exception {
        // Setup
        final CloudAppRocketProperties properties = new CloudAppRocketProperties();
        properties.setNameServer("nameServer");
        properties.setUsername("username");
        properties.setPassword("password");
        properties.setUseTLS(false);
        properties.setEnableMsgTrace(false);
        properties.setTraceTopic("traceTopic");
        final RocketConsumerProperties rocketConsumerProperties = new RocketConsumerProperties();
        rocketConsumerProperties.setType("type");
        rocketConsumerProperties.setName("name");
        rocketConsumerProperties.setNameServer("nameServer");
        rocketConsumerProperties.setUsername("username");
        rocketConsumerProperties.setPassword("password");
        rocketConsumerProperties.setUseTLS(false);
        rocketConsumerProperties.setEnableMsgTrace(false);
        rocketConsumerProperties.setTraceTopic("traceTopic");
        properties.setInputs(Arrays.asList(rocketConsumerProperties));
        final RocketProducerProperties rocketProducerProperties = new RocketProducerProperties();
        rocketProducerProperties.setNameServer("nameServer");
        rocketProducerProperties.setUsername("username");
        rocketProducerProperties.setPassword("password");
        rocketProducerProperties.setUseTLS(false);
        rocketProducerProperties.setEnableMsgTrace(false);
        rocketProducerProperties.setTraceTopic("traceTopic");
        rocketProducerProperties.setName("name");
        properties.setOutputs(Arrays.asList(rocketProducerProperties));
        properties.setAccessChannel("accessChannel");
        
        Field field = rocketBeanFactory.getClass().getDeclaredField("environment");
        field.setAccessible(true);
        field.set(rocketBeanFactory, new MockEnvironment());
        
        field = rocketBeanFactory.getClass().getDeclaredField("properties");
        field.setAccessible(true);
        field.set(rocketBeanFactory, properties);
        
        field = rocketBeanFactory.getClass().getDeclaredField("beanFactory");
        field.setAccessible(true);
        field.set(rocketBeanFactory, beanFactory);
        
        // Run the test
        rocketBeanFactory.refresh(properties);
        
        // Verify the results
    }
    
    @Test
    void testSetEnvironment() {
        // Setup
        final MockEnvironment environment = new MockEnvironment();
        
        environment.setProperty("io.cloudapp.messaging.rocketmq.nameServer", "nameServer");
        environment.setProperty("io.cloudapp.messaging.rocketmq.username", "username");
        environment.setProperty("io.cloudapp.messaging.rocketmq.password", "password");
        environment.setProperty("io.cloudapp.messaging.rocketmq.useTLS", "false");
        environment.setProperty("io.cloudapp.messaging.rocketmq.enableMsgTrace", "false");
        environment.setProperty("io.cloudapp.messaging.rocketmq.traceTopic", "traceTopic");
        environment.setProperty("io.cloudapp.messaging.rocketmq.accessChannel", "accessChannel");
        environment.setProperty("io.cloudapp.messaging.rocketmq.inputs[0].type", "type");
        environment.setProperty("io.cloudapp.messaging.rocketmq.inputs[0].nameServer", "nameServer");
        environment.setProperty("io.cloudapp.messaging.rocketmq.inputs[0].username", "username");
        environment.setProperty("io.cloudapp.messaging.rocketmq.inputs[0].password", "password");
        environment.setProperty("io.cloudapp.messaging.rocketmq.inputs[0].useTLS", "false");
        environment.setProperty("io.cloudapp.messaging.rocketmq.inputs[0].enableMsgTrace", "false");
        environment.setProperty("io.cloudapp.messaging.rocketmq.inputs[0].traceTopic", "traceTopic");
        
        // Run the test
        rocketBeanFactory.setEnvironment(environment);
        
        // Verify the results
    }
    
}
