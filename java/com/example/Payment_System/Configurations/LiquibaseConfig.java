package com.example.Payment_System.Configurations;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "app.liquibase.enabled", havingValue = "true")
public class LiquibaseConfig {

    @Bean(name = "liquibaseLegal")
    @ConditionalOnProperty(name = "app.liquibase.legal-enabled", havingValue = "true")
    public SpringLiquibase liquibaseLegal(@Qualifier("legalDataSource") javax.sql.DataSource ds) {
        var lb = new SpringLiquibase();
        lb.setDataSource(ds);
        lb.setChangeLog("classpath:db/changelog/changelog-master-legal.xml");
        return lb;
    }

    @Bean(name = "liquibaseIndividual")
    @ConditionalOnProperty(name = "app.liquibase.individual-enabled", havingValue = "true")
    public SpringLiquibase liquibaseIndividual(@Qualifier("individualDataSource") javax.sql.DataSource ds) {
        var lb = new SpringLiquibase();
        lb.setDataSource(ds);
        lb.setChangeLog("classpath:db/changelog/changelog-master-individual.xml");
        return lb;
    }
}




