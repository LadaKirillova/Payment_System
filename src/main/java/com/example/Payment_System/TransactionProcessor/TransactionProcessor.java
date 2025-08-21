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
import org.springframework.jms.annotation.JmsListener;
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

@Component
public class TransactionProcessor {

    private final TransactionService transactionService;

    public TransactionProcessor(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @JmsListener(destination = "transaction.queue")
    public void processTransaction(Transaction transaction) {
        transactionService.makePayment(transaction, transaction.getEntries());
        System.out.println("Payment processed successfully");
    }
}



