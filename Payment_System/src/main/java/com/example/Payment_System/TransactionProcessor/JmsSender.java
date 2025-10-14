package com.example.Payment_System.TransactionProcessor;

import com.example.Payment_System.Model.Transaction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import jakarta.jms.Message;
import jakarta.jms.Queue;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import java.util.UUID;
@Component
@ConditionalOnProperty(name="app.messaging.backend", havingValue="artemis")
class JmsSender implements MessageSender {

    private final JmsTemplate jmsTemplate;

    JmsSender(JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void send(Transaction transaction){
        // 👇 Самый простой способ: отправляем Transaction в очередь "transaction.queue"
        jmsTemplate.convertAndSend("transaction.queue", transaction);
        System.out.println("Отправлено в JMS: " + transaction);
    }
}