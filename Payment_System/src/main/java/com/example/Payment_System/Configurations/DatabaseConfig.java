package com.example.Payment_System.Configurations;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

//@Configuration
//public class DatabaseConfig {
//
//    @Bean
//    @Primary
//    public DataSource dataSource() {
//        return DataSourceBuilder.create()
//                .url("jdbc:postgresql://postgres:5432/Payment_system_prototype")
//                .username("LdKrlv")
//                .password("0987765")
//                .driverClassName("org.postgresql.Driver")
//                .build();
//    }
//
//    @Bean
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//
//    //Для работы @Transactional вместо JPA (закомментирована зависимость spring-boot-starter-data-jpa)
//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//}


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class DatabaseConfig {
//
//    @Bean
//    public Connection connection() throws SQLException {
//        String url = "jdbc:postgresql://postgres:5432/Payment_system_prototype";
//        String username = "LdKrlv";
//        String password = "0987765";
//        return DriverManager.getConnection(url, username, password);
//    }
//}

//@Configuration
////@PropertySource("classpath:application.yaml")
//public class DatabaseConfig {
//
//    @Bean
//    public DataSource dataSource(DataSourceProperties properties) {
//        return properties.initializeDataSourceBuilder()
//                .type(HikariDataSource.class)
//                .build();
//    }
//
//    @Bean
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//}
//
@Configuration
public class DatabaseConfig {

    @Bean(name = "legalDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.legal")
    public javax.sql.DataSource legalDataSource() {
        return org.springframework.boot.jdbc.DataSourceBuilder.create()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean(name = "individualDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.individual")
    public javax.sql.DataSource individualDataSource() {
        return org.springframework.boot.jdbc.DataSourceBuilder.create()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean(name = "legalJdbcTemplate")
    public org.springframework.jdbc.core.JdbcTemplate legalJdbcTemplate(
            @Qualifier("legalDataSource") javax.sql.DataSource ds) {
        return new org.springframework.jdbc.core.JdbcTemplate(ds);
    }

    @Bean(name = "individualJdbcTemplate")
    public org.springframework.jdbc.core.JdbcTemplate individualJdbcTemplate(
            @Qualifier("individualDataSource") javax.sql.DataSource ds) {
        return new org.springframework.jdbc.core.JdbcTemplate(ds);
    }
}
