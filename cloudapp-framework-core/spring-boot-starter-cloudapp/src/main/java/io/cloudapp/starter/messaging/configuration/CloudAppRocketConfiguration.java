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

package io.cloudapp.starter.messaging.configuration;

import io.cloudapp.messaging.rocketmq.CloudAppRocketProducer;
import io.cloudapp.starter.messaging.factory.CloudAppRocketBeanFactory;
import io.cloudapp.starter.messaging.properties.CloudAppRocketProperties;
import io.cloudapp.starter.messaging.refresh.RocketRefreshComponent;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass({RocketMQTemplate.class, CloudAppRocketProducer.class})
@EnableConfigurationProperties(CloudAppRocketProperties.class)
@ConditionalOnProperty(
        prefix = CloudAppRocketProperties.PREFIX,
        value = "enabled",
        havingValue = "true",
        matchIfMissing = true)
public class CloudAppRocketConfiguration {
    
    @Bean
    public RocketRefreshComponent rocketRefreshComponent(
            CloudAppRocketProperties properties,
            CloudAppRocketBeanFactory beanFactory
    ) {
        return new RocketRefreshComponent(properties, beanFactory);
    }
    
    @Bean
    public CloudAppRocketBeanFactory cloudAppRocketBeanFactory() {
        return new CloudAppRocketBeanFactory();
    }
    
}
