package com.example.Payment_System.Compliance;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComplianceConfig {

    @Bean
    public ComplianceClient complianceClient() {
        return new ComplianceClient();
    }
}
