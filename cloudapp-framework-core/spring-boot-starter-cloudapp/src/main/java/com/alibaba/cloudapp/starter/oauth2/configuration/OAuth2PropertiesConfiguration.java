package com.alibaba.cloudapp.starter.oauth2.configuration;

import com.alibaba.cloudapp.starter.oauth2.properties.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(OAuth2ClientProperties.class)
@ConditionalOnProperty(
        prefix = OAuth2Component.BINDING_PROP_KEY,
        value = "enabled",
        havingValue = "true")
@AutoConfigureBefore(OAuth2AutoConfiguration.class)
public class OAuth2PropertiesConfiguration {

}
