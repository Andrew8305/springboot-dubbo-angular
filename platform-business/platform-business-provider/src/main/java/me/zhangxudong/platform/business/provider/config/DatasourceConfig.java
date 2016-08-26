package me.zhangxudong.platform.business.provider.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据源配置
 * Created by zhangxd on 16/8/16.
 */
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DatasourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.druid")
    public DruidDataSource dataSource(
            DataSourceProperties properties) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(properties.determineDriverClassName());
        dataSource.setUrl(properties.determineUrl());
        dataSource.setUsername(properties.determineUsername());
        dataSource.setPassword(properties.determinePassword());
        DatabaseDriver databaseDriver = DatabaseDriver
                .fromJdbcUrl(properties.determineUrl());
        String validationQuery = databaseDriver.getValidationQuery();
        if (validationQuery != null) {
            dataSource.setTestOnBorrow(true);
            dataSource.setValidationQuery(validationQuery);
        }
        return dataSource;
    }

}
