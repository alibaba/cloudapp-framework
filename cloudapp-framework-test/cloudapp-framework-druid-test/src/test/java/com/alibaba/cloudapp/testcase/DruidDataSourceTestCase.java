/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cloudapp.testcase;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.cloudapp.datasource.druid.RefreshableDruidDataSourceWrapper;
import com.alibaba.cloudapp.demo.app.DemoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author alickreborn
 */
@SpringBootTest(classes = {DemoApplication.class})
public class DruidDataSourceTestCase implements ApplicationContextAware {
    @Resource
    private DruidDataSource dataSource;

    @Autowired
    private Environment env;

    private ApplicationContext applicationContext;

    @Test
    public void testDataSourceExists() throws Exception {
        assertThat(dataSource).isNotNull();

        assertThat(dataSource).isInstanceOf(RefreshableDruidDataSourceWrapper.class);
    }

    @Test
    public void testDataSourcePropertiesOverridden() throws Exception {
        assertThat(dataSource.getUrl()).isEqualTo("jdbc:h2:file:./demo-db;NON_KEYWORDS=user");
        assertThat(dataSource.getInitialSize()).isEqualTo(2);
        assertThat(dataSource.getMaxActive()).isEqualTo(30);
        assertThat(dataSource.getMinIdle()).isEqualTo(2);
        assertThat(dataSource.getMaxWait()).isEqualTo(1234);
        assertThat(dataSource.isPoolPreparedStatements()).isTrue();
        //assertThat(dataSource.getMaxOpenPreparedStatements()).isEqualTo(5); //Duplicated with following
        assertThat(dataSource.getMaxPoolPreparedStatementPerConnectionSize()).isEqualTo(5);
        assertThat(dataSource.getValidationQuery()).isEqualTo("select 1");
        assertThat(dataSource.getValidationQueryTimeout()).isEqualTo(1);
        assertThat(dataSource.isTestWhileIdle()).isTrue();
        assertThat(dataSource.isTestOnBorrow()).isTrue();
        assertThat(dataSource.isTestOnReturn()).isTrue();
        assertThat(dataSource.getTimeBetweenEvictionRunsMillis()).isEqualTo(10000);
        assertThat(dataSource.getMinEvictableIdleTimeMillis()).isEqualTo(30001);
        assertThat(dataSource.isAsyncCloseConnectionEnable()).isEqualTo(true);
    }


    @Test
    public void testDataSourceRefreshWhileHasActiveCount() throws SQLException, InterruptedException {
        assertThat(dataSource.getMinIdle()).isEqualTo(2);

        Connection conn1 = dataSource.getConnection();
        Connection conn2 = dataSource.getConnection();

        assertThat(dataSource.getActiveCount()).isEqualTo(2);
        conn1.close();

        triggerRebuildDataSource();

        assertThat(dataSource.getActiveCount()).isEqualTo(1);

        conn2.close();


        assertThat(dataSource.getActiveCount()).isEqualTo(0);
        assertThat(conn2.isClosed()).isEqualTo(true);

        // Thread.sleep(1000);
        conn2 = dataSource.getConnection();
        assertThat(dataSource.getActiveCount()).isEqualTo(1);

        boolean containUpdatedString = conn2.getMetaData().toString().contains("demo-updated-db");
        assertThat(containUpdatedString).isEqualTo(true);
    }



    @Test
    public void testDataSourceRefreshable() throws SQLException {
        assertThat(dataSource.getMinIdle()).isEqualTo(2);

        Connection conn1 = dataSource.getConnection();
        Connection conn2 = dataSource.getConnection();

        assertThat(dataSource.getActiveCount()).isEqualTo(2);
        conn1.close();
        assertThat(dataSource.getActiveCount()).isEqualTo(1);
        conn2.close();

        triggerRebuildDataSource();

        assertThat(dataSource.getMinIdle()).isEqualTo(3);
        assertThat(dataSource.getActiveCount()).isEqualTo(0);

        assertThat(conn2.isClosed()).isEqualTo(true);
    }

    private void triggerRebuildDataSource() {
        System.setProperty("spring.datasource.druid.min-idle", "3");
        System.setProperty("io.cloudapp.datasource.druid.manage-version", "1.1");

        // spring.datasource.url=jdbc:h2:file:./demo-db;NON_KEYWORDS=user
        System.setProperty("spring.datasource.url",
                "jdbc:h2:file:./demo-updated-db;NON_KEYWORDS=user");

        assertThat(env.getProperty("spring.datasource.druid.min-idle")).isEqualTo("3");
        assertThat(env.getProperty("io.cloudapp.datasource.druid.manage-version")).isEqualTo("1.1");

        RefreshEvent event = new RefreshEvent(this, null, "Refresh DataSource Config");
        applicationContext.publishEvent(event);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
