package com.example.Payment_System.TransactionProcessor;

import com.example.Payment_System.Model.Transaction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="app.messaging.backend", havingValue="artemis")
class JmsSender implements MessageSender {
    private final JmsTemplate jt;
    JmsSender(JmsTemplate jt){ this.jt = jt; }
    public void send(Transaction t){ jt.convertAndSend("transaction.queue", t); }
}