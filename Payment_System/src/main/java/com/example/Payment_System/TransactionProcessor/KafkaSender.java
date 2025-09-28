package com.example.Payment_System.TransactionProcessor;

import com.example.Payment_System.Model.Transaction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="app.messaging.backend", havingValue="kafka", matchIfMissing=true)
class KafkaSender implements MessageSender {
    private final KafkaTemplate<String, Transaction> kt;
    KafkaSender(KafkaTemplate<String, Transaction> kt){ this.kt = kt; }
    public void send(Transaction t){ kt.send("transactions", t); }
}