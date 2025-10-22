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
package com.alibaba.cloudapp.starter.datasource.configurations;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidFilterConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidSpringAopConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidStatViewServletConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidWebStatFilterConfiguration;
import com.alibaba.cloudapp.datasource.druid.RefreshableDruidDataSourceWrapper;
import com.alibaba.cloudapp.starter.datasource.properties.RefreshableDruidProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author lihengming [89921218@qq.com]
 */
@Configuration
@ConditionalOnClass({DruidDataSource.class, RefreshableDruidDataSourceWrapper.class})
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
@EnableConfigurationProperties({RefreshableDruidProperties.class,
        DruidStatProperties.class,
        DataSourceProperties.class})
@Import({DruidSpringAopConfiguration.class,
        DruidStatViewServletConfiguration.class,
        DruidWebStatFilterConfiguration.class,
        DruidFilterConfiguration.class})
public class RefreshableDruidDataSourceAutoConfigure {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshableDruidDataSourceAutoConfigure.class);

    /**
     * Not setting initMethod of annotation {@code @Bean} is to avoid failure when inspecting
     * the bean definition at the build time. The {@link DruidDataSource#init()} will be called
     * at the end of {@link DruidDataSourceWrapper#afterPropertiesSet()}.
     *
     * @return druid data source wrapper
     */
    @Bean
    @ConditionalOnClass(RefreshableDruidDataSourceWrapper.class)
    @ConditionalOnMissingBean({RefreshableDruidDataSourceWrapper.class})
    public DruidDataSourceWrapper refreshableDataSource(
            RefreshableDruidProperties properties
    ) {
        LOGGER.info("Init DruidDataSource");
        RefreshableDruidDataSourceWrapper wrapper = new RefreshableDruidDataSourceWrapper();

        long maxWaitMillis = properties.getMaxRefreshWaitSeconds() * 1000L;
        wrapper.setMaxRefreshWaitMillis(maxWaitMillis);

        return wrapper;
    }

}
