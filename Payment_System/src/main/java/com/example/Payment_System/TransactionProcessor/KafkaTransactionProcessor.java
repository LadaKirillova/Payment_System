package com.example.Payment_System.TransactionProcessor;

import com.example.Payment_System.Model.Transaction;
import com.example.Payment_System.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(
        name = "app.messaging.backend",
        havingValue = "kafka",
        matchIfMissing = true
)
public class KafkaTransactionProcessor {
    private final TransactionService transactionService;

    @org.springframework.kafka.annotation.KafkaListener(
            topics = "transactions",
            groupId = "payment-processing",
            concurrency = "3"
    )
    public void processTransaction(Transaction transaction) throws Exception {
        log.info("Processing (Kafka) in {}", Thread.currentThread().getName());
        Thread.sleep(10_000);
        transactionService.makePayment(transaction, transaction.getEntries());
        log.info("Processed by {}", Thread.currentThread().getName());
    }
}

//Слушает очередь/топик и ждет сообщения
//
//Обрабатывает транзакции когда они приходят

