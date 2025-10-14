package com.example.Payment_System.TransactionProcessor;

import com.example.Payment_System.Model.Receipt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Отправляет квитанции (подтверждения) в Kafka топик "receipts".
 */
@Component
@ConditionalOnProperty(name="app.messaging.backend", havingValue="kafka", matchIfMissing=true)
public class KafkaReceiptSender {

    private final KafkaTemplate<String, Receipt> kafkaTemplate;

    public KafkaReceiptSender(KafkaTemplate<String, Receipt> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Receipt receipt){
        // 👇 Просто кладём квитанцию в топик "receipts"
        kafkaTemplate.send("receipts", receipt);
    }
}