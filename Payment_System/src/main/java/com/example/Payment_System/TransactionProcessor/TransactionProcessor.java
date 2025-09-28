package com.example.Payment_System.TransactionProcessor;

import com.example.Payment_System.DAO.TransactionDao;
import com.example.Payment_System.Model.Transaction;
import com.example.Payment_System.Service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import jakarta.jms.Message; // Основной интерфейс JMS-сообщения
import jakarta.jms.TextMessage; // Для работы с текстовыми сообщениями
import jakarta.jms.Session;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
//public class TransactionProcessor {
//
//    private final TransactionService transactionService;
//    private static final Logger logger = LoggerFactory.getLogger(TransactionProcessor.class);
//
//    public TransactionProcessor(TransactionService transactionService) {
//        this.transactionService = transactionService;
//    }
//
//    @Profile("!migration")
//    @JmsListener(
//            destination = "transaction.queue",
//            containerFactory = "jmsListenerContainerFactory",
//            concurrency = "3-3"
//    )
//    public void processTransaction(Transaction transaction) {
//        try {
//            logger.info("Processing transaction in thread: {}", Thread.currentThread().getName());
//
//            // Временная задержка для тестирования - 10 секунд
//            Thread.sleep(10000);
//
//            transactionService.makePayment(transaction, transaction.getEntries());
//            logger.info("Payment processed successfully by thread: {}", Thread.currentThread().getName());
//        } catch (Exception e) {
//            logger.error("Error processing transaction: {}", e.getMessage());
//        }
//    }
//}
//

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.messaging.backend", havingValue = "artemis")
public class TransactionProcessor {

    private final TransactionService transactionService;

    @JmsListener(
            destination = "transaction.queue",
            containerFactory = "jmsListenerContainerFactory",
            concurrency = "3-3"
    )
    public void process(Transaction transaction) throws Exception {
        log.info("Processing (JMS) in {}", Thread.currentThread().getName());
        Thread.sleep(10_000);
        transactionService.makePayment(transaction, transaction.getEntries());
        log.info("Processed (JMS)");
    }
}
