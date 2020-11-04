package com.example.cleandata.config;

import com.example.cleandata.properties.DataSourceOneProperties;
import com.example.cleandata.properties.DataSourceThreeProperties;
import com.example.cleandata.properties.DataSourceTwoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * @author QiuHongLong
 */
@Configuration
public class DatabaseConfig {


    @Autowired
    DataSourceOneProperties dataSourceOneProperties;

    @Autowired
    DataSourceTwoProperties dataSourceTwoProperties;

    @Autowired
    DataSourceThreeProperties dataSourceThreeProperties;


    @Bean
    public DataSource dataSourceOne() {
        return DataSourceBuilder.create()
                .driverClassName(dataSourceOneProperties.getDriverClassName())
                .url(dataSourceOneProperties.getUrl())
                .username(dataSourceOneProperties.getUsername())
                .password(dataSourceOneProperties.getPassword())
                .build();
    }


    @Bean
    public JdbcTemplate jdbcTemplateOne(@Qualifier("dataSourceOne") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public TransactionTemplate transactionTemplateOne(@Qualifier("dataSourceOne") DataSource dataSource) {
        return new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }

    @Bean
    public DataSource dataSourceTwo() {
        return DataSourceBuilder.create()
                .driverClassName(dataSourceTwoProperties.getDriverClassName())
                .url(dataSourceTwoProperties.getUrl())
                .username(dataSourceTwoProperties.getUsername())
                .password(dataSourceTwoProperties.getPassword())
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplateTwo(@Qualifier("dataSourceTwo") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public TransactionTemplate transactionTemplateTwo(@Qualifier("dataSourceTwo") DataSource dataSource) {
        return new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }


    @Bean
    public DataSource dataSourceThree() {
        return DataSourceBuilder.create()
                .driverClassName(dataSourceThreeProperties.getDriverClassName())
                .url(dataSourceThreeProperties.getUrl())
                .username(dataSourceThreeProperties.getUsername())
                .password(dataSourceThreeProperties.getPassword())
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplateThree(@Qualifier("dataSourceThree") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public TransactionTemplate transactionTemplateThree(@Qualifier("dataSourceThree") DataSource dataSource) {
        return new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }



}
