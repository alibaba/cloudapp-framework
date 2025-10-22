package com.alibaba.cloudapp.starter.oauth2.configuration;

import com.alibaba.cloudapp.api.oauth2.AuthorizationService;
import com.alibaba.cloudapp.starter.oauth2.properties.OAuth2ClientProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2ComponentTest {
    
    @Mock
    private OAuth2ClientProperties mockProperties;
    
    private OAuth2Component component;
    
    @BeforeEach
    void setUp() {
        component = new OAuth2Component(mockProperties);
    }
    
    @Test
    void testPostStart() {
        // Setup
        when(mockProperties.getClientId()).thenReturn("result");
        when(mockProperties.getClientSecret()).thenReturn("result");
        when(mockProperties.getGrantTypes()).thenReturn(Arrays.asList("value"));
        when(mockProperties.getScopes()).thenReturn(Arrays.asList("value"));
        
        // Run the test
        component.postStart();
        
        // Verify the results
    }
    
    @Test
    void testPostStart_OAuth2ClientPropertiesGetGrantTypesReturnsNull() {
        // Setup
        when(mockProperties.getClientId()).thenReturn("result");
        when(mockProperties.getClientSecret()).thenReturn("result");
        when(mockProperties.getGrantTypes()).thenReturn(null);
        
        // Run the test
        assertThrows(IllegalArgumentException.class,
                     () -> component.postStart()
        );
    }
    
    @Test
    void testPostStart_OAuth2ClientPropertiesGetGrantTypesReturnsNoItems() {
        // Setup
        when(mockProperties.getClientId()).thenReturn("result");
        when(mockProperties.getClientSecret()).thenReturn("result");
        when(mockProperties.getGrantTypes())
                .thenReturn(Collections.emptyList());
        
        // Run the test
        assertThrows(IllegalArgumentException.class,
                     () -> component.postStart()
        );
    }
    
    @Test
    void testPostStart_OAuth2ClientPropertiesGetScopesReturnsNull() {
        // Setup
        when(mockProperties.getClientId()).thenReturn("result");
        when(mockProperties.getClientSecret()).thenReturn("result");
        when(mockProperties.getGrantTypes()).thenReturn(Arrays.asList("value"));
        when(mockProperties.getScopes()).thenReturn(null);
        
        // Run the test
        assertThrows(IllegalArgumentException.class,
                     () -> component.postStart()
        );
    }
    
    @Test
    void testPostStart_OAuth2ClientPropertiesGetScopesReturnsNoItems() {
        // Setup
        when(mockProperties.getClientId()).thenReturn("result");
        when(mockProperties.getClientSecret()).thenReturn("result");
        when(mockProperties.getGrantTypes()).thenReturn(Arrays.asList("value"));
        when(mockProperties.getScopes()).thenReturn(Collections.emptyList());
        when(mockProperties.getScopes()).thenReturn(Arrays.asList("value"));
        
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
        assertEquals("io.cloudapp.oauth2", component.bindKey());
    }
    
    @Test
    void testGetName() {
        assertEquals("cloudappAuthorizationService",
                     component.getName()
        );
    }
    
    @Test
    void testCreateBean() {
        // Setup
        final OAuth2ClientProperties properties = new OAuth2ClientProperties();
        properties.setClientId("clientId");
        properties.setClientSecret("clientSecret");
        properties.setScopes(Arrays.asList("value"));
        properties.setRedirectUri("redirectUri");
        properties.setGrantTypes(Arrays.asList("value"));
        
        // Run the test
        final AuthorizationService result = component.createBean(
                properties);
        
        // Verify the results
    }
    
}
