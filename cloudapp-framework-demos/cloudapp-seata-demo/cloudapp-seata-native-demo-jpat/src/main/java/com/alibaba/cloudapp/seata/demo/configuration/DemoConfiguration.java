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

package com.alibaba.cloudapp.seata.demo.configuration;

import io.seata.rm.datasource.DataSourceProxy;
import org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DemoConfiguration {
    
    protected Map<String, Object> getVendorProperties(
            ConfigurableListableBeanFactory beanFactory) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("hibernate.hbm2ddl.auto", "update");
        map.put("hibernate.id.new_generator_mappings", "true");
        map.put("hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"
        );
        map.put("hibernate.resource.beans.container",
                new SpringBeanContainer(beanFactory)
        );
        map.put("hibernate.implicit_naming_strategy",
                "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy"
        );
        map.put("hibernate.archive.scanner",
                "org.hibernate.boot.archive.scan.internal.DisabledScanner"
        );
        map.put("hibernate.transaction.jta.platform", new NoJtaPlatform());
        
        return map;
    }
    
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder factoryBuilder,
            DataSource datasource,
            ConfigurableListableBeanFactory beanFactory) {
        Map<String, Object> vendorProperties = getVendorProperties(beanFactory);
        
        return factoryBuilder
                .dataSource(new DataSourceProxy(datasource))
                .packages("io.cloudapp.seata.demo")
                .properties(vendorProperties)
                .jta(false)
                .build();
    }
    
    
}
