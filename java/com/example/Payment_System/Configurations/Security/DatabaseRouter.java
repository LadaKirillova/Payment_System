package com.example.Payment_System.Configurations.Security;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;



import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;



import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class DatabaseRouter {
    private final JdbcTemplate legalJdbcTemplate;
    private final JdbcTemplate individualJdbcTemplate;

    public DatabaseRouter(
            @Qualifier("legalJdbcTemplate") JdbcTemplate legalJdbcTemplate,
            @Qualifier("individualJdbcTemplate") JdbcTemplate individualJdbcTemplate
    ) {
        this.legalJdbcTemplate = legalJdbcTemplate;
        this.individualJdbcTemplate = individualJdbcTemplate;
    }

    public JdbcTemplate getTemplate(String accountNumber) {
        if (accountNumber.startsWith("4")) {
            return legalJdbcTemplate;
        } else if (accountNumber.startsWith("5")) {
            return individualJdbcTemplate;
        } else {
            throw new IllegalArgumentException("Unknown account type: " + accountNumber);
        }
    }

    public JdbcTemplate getTemplateByType(String type) {
        if ("LEGAL".equalsIgnoreCase(type)) {
            return legalJdbcTemplate;
        } else if ("INDIVIDUAL".equalsIgnoreCase(type)) {
            return individualJdbcTemplate;
        } else {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}

