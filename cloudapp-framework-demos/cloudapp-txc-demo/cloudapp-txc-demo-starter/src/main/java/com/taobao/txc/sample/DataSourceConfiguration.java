package com.taobao.txc.sample;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

import com.taobao.txc.datasource.cobar.TxcDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "my.datasource.a")
    public DruidDataSource dataSourceA() {
        System.out.println("初始化数据源A......");
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

    @Bean
    public TxcDataSource dataSourceProxyA(DruidDataSource dataSourceA) {
        // 用 TxcDataSource 包装代理我的数据源 A
        System.out.println("将数据源A包装为GTS数据源.....");
        return new TxcDataSource(dataSourceA);
    }

    @Bean
    public JdbcTemplate jdbcTemplateA(TxcDataSource dataSourceProxyA) {
        return new JdbcTemplate(dataSourceProxyA);
    }

    @Bean
    @ConfigurationProperties(prefix = "my.datasource.b")
    public DruidDataSource dataSourceB() {
        // 我的数据源 B
        System.out.println("初始化数据源B......");
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

    @Bean
    public TxcDataSource dataSourceProxyB(DruidDataSource dataSourceB) {
        // 用 TxcDataSource 包装代理我的数据源 B
        System.out.println("将数据源B包装为GTS数据源.....");
        return new TxcDataSource(dataSourceB);
    }

    @Bean
    public JdbcTemplate jdbcTemplateB(TxcDataSource dataSourceProxyB) {
        return new JdbcTemplate(dataSourceProxyB);
    }
}
