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

package io.cloudapp.starter.sequence.configuration;

import io.cloudapp.sequence.Constants;
import io.cloudapp.sequence.service.SnowflakeSequenceGenerator;
import io.cloudapp.starter.sequence.properties.CloudAppSequenceProperties;
import io.cloudapp.starter.sequence.refresh.SnowflakeSequenceRefreshComponent;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(CloudAppSequenceProperties.class)
@ConditionalOnProperty(prefix = Constants.SEQUENCE_CONFIG_SNOWFLAKE_ROOT,
        name = "enabled",
        havingValue = "true")
public class SnowflakeSequenceAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public SnowflakeSequenceRefreshComponent getComponent(
            CloudAppSequenceProperties properties
    ) {
        return new SnowflakeSequenceRefreshComponent(properties);
    }

    @Bean("snowflakeSequence")
    @ConditionalOnMissingBean(
            value = SnowflakeSequenceGenerator.class,
            name = "snowflakeSequence"
    )
    public SnowflakeSequenceGenerator getRedisSequence(
            SnowflakeSequenceRefreshComponent component
    ) {
        return component.getBean();
    }
}
