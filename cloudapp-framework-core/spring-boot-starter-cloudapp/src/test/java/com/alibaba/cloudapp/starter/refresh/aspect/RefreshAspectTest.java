package com.alibaba.cloudapp.starter.refresh.aspect;

import com.alibaba.cloudapp.starter.base.properties.RefreshableProperties;
import com.alibaba.cloudapp.starter.refresh.PropKeyRefreshedEvent;
import com.alibaba.cloudapp.starter.refresh.TargetRefreshable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshAspectTest {
    
    private RefreshAspect aspect;
    @Mock
    private ProceedingJoinPoint joinPoint;
    @Mock
    private RefreshableProperties properties;
    
    @BeforeEach
    void setUp() {
        aspect = new RefreshAspect();
    }
    
    @Test
    void testRefreshPointcut() throws Throwable {
        // Setup
        MethodSignature signature = mock(MethodSignature.class);
        Method method = TestClass.class.getMethod("testMethod");
        RefreshableBinding binding = mock(RefreshableBinding.class);
        
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(method);
        when(signature.getName()).thenReturn("value");
        when(joinPoint.proceed()).thenReturn(new TargetRefreshable<RefreshableProperties>() {
            
            @Override
            public void refreshTarget(RefreshableProperties target) {}
            
            @Override
            public Object getTarget() {
                return properties;
            }
        });
        
        // Run the test
        final Object result = aspect.refreshPointcut(joinPoint);
        
        // Verify the results
    }
    
    @Test
    void testRefreshPointcut_ThrowsThrowable() {
        // Setup
        
        // Run the test
        assertThrows(Throwable.class,
                     () -> aspect.refreshPointcut(joinPoint)
        );
    }
    
    @Test
    void testOnApplicationEvent() {
        // Setup
        final PropKeyRefreshedEvent event = new PropKeyRefreshedEvent(
                "source", properties
        );
        
        // Run the test
        aspect.onApplicationEvent(event);
        
        // Verify the results
    }
    
    public static class TestClass {
        
        @RefreshableBinding("test")
        public void testMethod() {
        }
    }
    
}
