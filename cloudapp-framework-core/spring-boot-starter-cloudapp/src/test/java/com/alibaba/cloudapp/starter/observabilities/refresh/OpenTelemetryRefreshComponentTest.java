package com.alibaba.cloudapp.starter.observabilities.refresh;

import com.alibaba.cloudapp.observabilities.opentelemetry.metric.GlobalOpenTelemetry;
import com.alibaba.cloudapp.observabilities.opentelemetry.metric.MetricCollectionAspect;
import com.alibaba.cloudapp.observabilities.opentelemetry.metric.MetricHelperServer;
import com.alibaba.cloudapp.starter.observabilities.properties.OpenTelemetryProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OpenTelemetryRefreshComponentTest {
    
    @Mock
    private OpenTelemetryProperties mockProperties;
    
    private OpenTelemetryRefreshComponent component;
    
    @BeforeEach
    void setUp() {
        component = new OpenTelemetryRefreshComponent(mockProperties);
    }
    
    @Test
    void testPostStart() {
        // Setup
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
        assertEquals("io.cloudapp.observabilities.ot",
                     component.bindKey()
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("otMetricCollectionAspect",
                     component.getName()
        );
    }
    
    @Test
    void testCreateBean() {
        // Setup
        final OpenTelemetryProperties properties = new OpenTelemetryProperties();
        properties.setMetricEndpoint("metricEndpoint");
        properties.setDefaultServiceName("defaultServiceName");
        properties.setTimeout(0);
        properties.setInterval(0);
        properties.setEnabledHelperServer(false);
        properties.setHelperServerPort(0);
        properties.setHelperServerBindAddress("helperServerAddress");
        properties.setPrometheusPort(0);
        properties.setPrometheusExporter(false);
        
        // Run the test
        final GlobalOpenTelemetry result = component.createBean(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testCreateServer() {
        // Setup
        final OpenTelemetryProperties properties = new OpenTelemetryProperties();
        properties.setMetricEndpoint("metricEndpoint");
        properties.setDefaultServiceName("defaultServiceName");
        properties.setTimeout(0);
        properties.setInterval(0);
        properties.setEnabledHelperServer(false);
        properties.setHelperServerPort(0);
        properties.setHelperServerBindAddress("helperServerAddress");
        properties.setPrometheusPort(0);
        properties.setPrometheusExporter(false);
        
        // Run the test
        final MetricHelperServer result = component.createServer(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testCreateAspect() {
        // Setup
        final OpenTelemetryProperties properties = new OpenTelemetryProperties();
        properties.setMetricEndpoint("metricEndpoint");
        properties.setDefaultServiceName("defaultServiceName");
        properties.setTimeout(0);
        properties.setInterval(0);
        properties.setEnabledHelperServer(false);
        properties.setHelperServerPort(0);
        properties.setHelperServerBindAddress("helperServerAddress");
        properties.setPrometheusPort(0);
        properties.setPrometheusExporter(false);
        
        // Run the test
        final GlobalOpenTelemetry result = component.createOpenTelemetry(
                properties);
        
        // Verify the results
    }
    
    @Test
    void testGetServer() {
        final MetricHelperServer result = component.getServer();
    }
    
}
