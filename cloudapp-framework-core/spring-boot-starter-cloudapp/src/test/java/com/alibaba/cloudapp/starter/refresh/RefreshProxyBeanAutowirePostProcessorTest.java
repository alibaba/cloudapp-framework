package com.alibaba.cloudapp.starter.refresh;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.core.Ordered;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RefreshProxyBeanAutowirePostProcessorTest {
    
    private RefreshProxyBeanAutowirePostProcessor postProcessor;
    
    @BeforeEach
    void setUp() {
        postProcessor = new RefreshProxyBeanAutowirePostProcessor();
    }
    
    @Test
    void testGetOrder() {
        assertEquals(Ordered.HIGHEST_PRECEDENCE, postProcessor.getOrder());
    }
    
    @Test
    void testPostProcessProperties() {
        // Setup
        final PropertyValues pvs = null;
        
        // Run the test
        final PropertyValues result = postProcessor.postProcessProperties(
                pvs, "bean", "beanName");
        
        // Verify the results
    }
    
    @Test
    void testProcessInjection() {
        // Setup
        // Run the test
        postProcessor.processInjection("bean");
        
        // Verify the results
    }
    
}
