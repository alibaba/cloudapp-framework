package com.alibaba.cloudapp.starter.mail.refresh;

import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.starter.mail.properties.CloudAppMailProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Session;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailSenderRefreshableComponentTest {
    
    @Mock
    private CloudAppMailProperties mockProperties;
    @Mock
    private BeanFactory mockBeanFactory;
    
    private MailSenderRefreshableComponent component;
    
    @BeforeEach
    void setUp() {
        component = new MailSenderRefreshableComponent(
                mockProperties);
        component.setBeanFactory(mockBeanFactory);
    }
    
    @Test
    void testPostStart() {
        // Setup
        when(mockProperties.getUsername()).thenReturn("username");
        when(mockProperties.getPassword()).thenReturn("password");
        when(mockProperties.getProtocol()).thenReturn("protocol");
        when(mockProperties.getDefaultEncoding())
                .thenReturn(StandardCharsets.UTF_8);
        when(mockProperties.getProperties()).thenReturn(new HashMap<>());
        
        // Run the test
        component.postStart();
        
        // Verify the results
    }
    
    @Test
    void testPostStart_CloudAppMailPropertiesGetDefaultEncodingReturnsNull() {
        // Setup
        when(mockProperties.getUsername()).thenReturn("username");
        when(mockProperties.getPassword()).thenReturn("password");
        when(mockProperties.getProtocol()).thenReturn("protocol");
        when(mockProperties.getDefaultEncoding()).thenReturn(null);
        when(mockProperties.getProperties()).thenReturn(new HashMap<>());
        
        // Run the test
        component.postStart();
        
        // Verify the results
    }
    
    @Test
    void testPreStop() {
        component.preStop();
    }
    
    @Test
    void testBindKey() {
        assertEquals("io.cloudapp.mail",
                     component.bindKey()
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("cloudAppMail",
                     component.getName()
        );
    }
    
    @Test
    void testCreateBean() {
        // Setup
        final CloudAppMailProperties properties = new CloudAppMailProperties();
        properties.setHost("host");
        properties.setPort(0);
        properties.setUsername("username");
        properties.setPassword("password");
        properties.setProtocol("protocol");
        properties.setDefaultEncoding(StandardCharsets.UTF_8);
        properties.setProperties(new HashMap<>());
        
        when(mockProperties.getUsername()).thenReturn("username");
        when(mockProperties.getPassword()).thenReturn("password");
        when(mockProperties.getProtocol()).thenReturn("protocol");
        when(mockProperties.getDefaultEncoding())
                .thenReturn(StandardCharsets.UTF_8);
        when(mockProperties.getProperties()).thenReturn(new HashMap<>());
        
        // Run the test
        final JavaMailSenderImpl result = component.createBean(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testCreateBean_CloudAppMailPropertiesGetDefaultEncodingReturnsNull() {
        // Setup
        final CloudAppMailProperties properties = new CloudAppMailProperties();
        properties.setHost("host");
        properties.setPort(0);
        properties.setUsername("username");
        properties.setPassword("password");
        properties.setProtocol("protocol");
        properties.setDefaultEncoding(StandardCharsets.UTF_8);
        properties.setProperties(new HashMap<>());
        properties.setJndiName("jndiName");
        
        // Run the test
        assertThrows(CloudAppException.class, () -> component.createBean(
                properties));
        
        // Verify the results
    }
    
    @Test
    void testRefreshSession() {
        // Setup
        final CloudAppMailProperties properties = new CloudAppMailProperties();
        properties.setHost("host");
        properties.setPort(0);
        properties.setUsername("username");
        properties.setPassword("password");
        properties.setProtocol("protocol");
        properties.setDefaultEncoding(StandardCharsets.UTF_8);
        properties.setProperties(new HashMap<>());
        properties.setJndiName("jndi");
        
        // Run the test
        assertThrows(CloudAppException.class, () ->
                component.refreshSession(properties));
        
        // Verify the results
    }
    
    @Test
    void testGetSession() {
        final Session result = component.getSession();
    }
    
}
