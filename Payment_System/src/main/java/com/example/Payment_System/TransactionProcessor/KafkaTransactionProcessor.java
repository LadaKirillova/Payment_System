package com.example.Payment_System.TransactionProcessor;

import com.example.Payment_System.Compliance.ComplianceClient;
import com.example.Payment_System.Model.Receipt;
import com.example.Payment_System.Model.Transaction;
import com.example.Payment_System.Service.OutboxService;
import com.example.Payment_System.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.messaging.backend", havingValue = "kafka", matchIfMissing = true)
public class KafkaTransactionProcessor {

    private final TransactionService transactionService;
    private final OutboxService outboxService;
    private final ComplianceClient complianceClient; // gRPC - клиент

    @KafkaListener(topics = "transactions", groupId = "payment-processing", concurrency = "3")
    @Transactional
    public void processTransaction(Transaction transaction) {
        log.info("Kafka получил транзакцию: {}", transaction);

        // === ДОБАВИЛИ ПРОВЕРКУ ===
        boolean approved = complianceClient.isApproved(
                transaction.getAccount_sender()
        );

        if (!approved) {
            log.warn("Транзакция отклонена compliance");

            Receipt receipt = new Receipt(
                    String.valueOf(transaction.getTransaction_id()),
                    "ERROR",
                    "COMPLIANCE_REJECTED"
            );

            outboxService.save(receipt);
            return;
        }

        try {
            transactionService.makePayment(
                    transaction,
                    transaction.getEntries()
            );

            Receipt receipt = new Receipt(
                    String.valueOf(transaction.getTransaction_id()),
                    "OK",
                    null
            );

            outboxService.save(receipt);
            log.info("Транзакция обработана и квитанция добавлена в outbox");

        } catch (Exception e) {
            log.error("Ошибка обработки транзакции: {}", e.getMessage());
        }
    }
}