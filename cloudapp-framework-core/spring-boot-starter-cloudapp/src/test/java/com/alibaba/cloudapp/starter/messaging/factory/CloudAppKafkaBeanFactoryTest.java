package com.alibaba.cloudapp.starter.messaging.factory;

import com.alibaba.cloudapp.messaging.kafka.properties.KafkaConsumerProperties;
import com.alibaba.cloudapp.messaging.kafka.properties.KafkaProducerProperties;
import com.alibaba.cloudapp.starter.messaging.properties.CloudAppKafkaProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloudAppKafkaBeanFactoryTest {
    
    private CloudAppKafkaBeanFactory beanFactory;
    
    @Mock
    private ConfigurableListableBeanFactory factory;
    
    @BeforeEach
    void setUp() {
        beanFactory = new CloudAppKafkaBeanFactory();
    }
    
    @Test
    void testPostProcessBeanFactory_ThrowsBeansException()
            throws Exception {
        
        doThrow(new BeanCreationException(""))
                .when(factory)
                .registerSingleton(anyString(), any());
        
        when(factory.containsBean(anyString())).thenReturn(false);
        
        Field field = beanFactory.getClass().getDeclaredField("properties");
        field.setAccessible(true);
        
        CloudAppKafkaProperties properties = new CloudAppKafkaProperties();
        
        KafkaConsumerProperties consumer = new KafkaConsumerProperties();
        consumer.setName("name");
        consumer.setBootstrapServers("bootstrapServers");
        consumer.setType("type");
        consumer.setProperties(new HashMap<>());
        
        properties.setInputs(Collections.singletonList(consumer));
        field.set(beanFactory, properties);
        
        field = beanFactory.getClass().getDeclaredField("authConfig");
        field.setAccessible(true);
        field.set(beanFactory, new HashMap<>());
        // Run the test
        assertThrows(BeansException.class,
                     () -> this.beanFactory.postProcessBeanFactory(factory)
        );
    }
    
    @Test
    void testRefresh() throws Exception {
        // Setup
        final CloudAppKafkaProperties properties = new CloudAppKafkaProperties();
        properties.setMechanism("mechanism");
        properties.setServers("bootstrapServers");
        final KafkaProperties.Ssl ssl = new KafkaProperties.Ssl();
        properties.setSsl(ssl);
        final KafkaConsumerProperties kafkaConsumerProperties = new KafkaConsumerProperties();
        kafkaConsumerProperties.setName("name");
        kafkaConsumerProperties.setBootstrapServers("bootstrapServers");
        kafkaConsumerProperties.setProperties(new HashMap<>());
        kafkaConsumerProperties.setType("type");
        properties.setInputs(Arrays.asList(kafkaConsumerProperties));
        final KafkaProducerProperties kafkaProducerProperties = new KafkaProducerProperties();
        kafkaProducerProperties.setName("name");
        kafkaProducerProperties.setTopic("topic");
        kafkaProducerProperties.setProperties(new HashMap<>());
        kafkaProducerProperties.setBootstrapServers("bootstrapServers");
        properties.setOutputs(Arrays.asList(kafkaProducerProperties));
        properties.setUsername("username");
        properties.setPassword("password");
        properties.setSecurityProtocol("securityProtocol");
        properties.setIdentificationAlgorithm("identificationAlgorithm");
        
        when(factory.containsBean(anyString())).thenReturn(false);
        
        Field field = beanFactory.getClass().getDeclaredField("beanFactory");
        field.setAccessible(true);
        field.set(beanFactory, factory);
        
        field = beanFactory.getClass().getDeclaredField("properties");
        field.setAccessible(true);
        field.set(beanFactory, new CloudAppKafkaProperties());
        
        // Run the test
        beanFactory.refresh(properties);
        
        // Verify the results
    }
    
    @Test
    void testSetEnvironment() {
        // Setup
        final MockEnvironment environment = new MockEnvironment();
        environment.setProperty("io.cloudapp.messaging.kafka.servers", "bootstrapServers");
        environment.setProperty("io.cloudapp.messaging.kafka.inputs[0].name", "name");
        environment.setProperty("io.cloudapp.messaging.kafka.inputs[0].bootstrapServers", "bootstrapServers");
        environment.setProperty("io.cloudapp.messaging.kafka.inputs[0].type", "type");
        environment.setProperty("io.cloudapp.messaging.kafka.outputs[0].name", "name");
        environment.setProperty("io.cloudapp.messaging.kafka.outputs[0].topic", "topic");
        
        
        // Run the test
        beanFactory.setEnvironment(environment);
        
        // Verify the results
    }
    
    @Test
    void testSetEnvironment_ThrowsBeansException() {
        // Setup
        final Environment environment = new MockEnvironment();
        
        // Run the test
        assertThrows(NoSuchElementException.class,
                     () -> beanFactory.setEnvironment(environment)
        );
    }
    
}
