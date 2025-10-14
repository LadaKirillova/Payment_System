package com.example.Payment_System.TransactionProcessor;

import com.example.Payment_System.Model.Transaction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

//@Component
//@ConditionalOnProperty(name="app.messaging.backend", havingValue="kafka", matchIfMissing=true)
//class KafkaSender implements MessageSender {
//    private final KafkaTemplate<String, Transaction> kt;
//    KafkaSender(KafkaTemplate<String, Transaction> kt){ this.kt = kt; }
//    public void send(Transaction t){ kt.send("transactions", t); }
//}
// Компонент Spring — автоматически создаётся и внедряется

/**
 * Отправляет транзакции в Kafka.
 * Использует KafkaTemplate из Spring Boot.
 * Активируется, если в application.yml указано app.messaging.backend=kafka.
 */
@Component
@ConditionalOnProperty(name="app.messaging.backend", havingValue="kafka", matchIfMissing=true)
class KafkaSender implements MessageSender {

    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    KafkaSender(KafkaTemplate<String, Transaction> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(Transaction transaction) {
        //  отправка объекта Transaction в топик "transactions"
        kafkaTemplate.send("transactions", transaction);
        System.out.println("Отправлено в Kafka: " + transaction);
    }
}



