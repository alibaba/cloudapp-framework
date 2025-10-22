package com.alibaba.cloudapp.starter.refresh;

import com.alibaba.cloudapp.starter.base.properties.RefreshableProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RefreshableProxyFactoryTest {
    
    @Mock
    private RefreshableProperties properties;
    private Object proxy;
    
    @BeforeEach
    void setUp() {
        proxy = RefreshableProxyFactory.create(this::create, properties);
    }
    
    @Test
    void testUpdateProxyTarget() throws Exception {
        // Run the test
        RefreshableProxyFactory.updateProxyTarget(proxy, properties);
        
        // Verify the results
    }
    
    @Test
    void testCreate1() {
        // Setup
        final Function<RefreshableProperties, Object> func = val -> new Object();
        
        // Run the test
        final Object result = RefreshableProxyFactory.create(func, properties);
        
        // Verify the results
        assert result != null;
    }
    
    @Test
    void testCreate2() {
        // Setup
        final Function<RefreshableProperties, Object> func = val -> new Object();
        final Consumer<Object> mockCallback = mock(Consumer.class);
        
        // Run the test
        final Object result = RefreshableProxyFactory.create(
                func, properties, mockCallback);
        
        // Verify the results
        assert result != null;
    }

    private Object create(RefreshableProperties props) {
        return new Object();
    }
}
