package com.example.Payment_System.TransactionProcessor;


import com.example.Payment_System.Model.Receipt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="app.messaging.backend", havingValue="artemis")
public class JmsReceiptSender {

    private final JmsTemplate jmsTemplate;

    public JmsReceiptSender(JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
    }

    public void send(Receipt receipt){
        // 👇 Самый простой вызов: отправляем квитанцию в очередь "receipt.queue"
        jmsTemplate.convertAndSend("receipt.queue", receipt);
    }
}