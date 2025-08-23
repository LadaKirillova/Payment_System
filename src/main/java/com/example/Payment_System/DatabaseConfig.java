package com.example.Payment_System;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

@Configuration
@PropertySource("classpath:application.yaml")
public class DatabaseConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

