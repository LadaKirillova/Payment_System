package com.example.Payment_System.Configurations;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopics {
    @Bean
    public NewTopic transactionsTopic() {
        return TopicBuilder.name("transactions")
                .partitions(3)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic receiptsTopic() {
        return TopicBuilder.name("receipts")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
