package com.example.Payment_System.Controller;

import com.example.Payment_System.Model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class TransactionController {

    private final JmsTemplate jmsTemplate;

    @Autowired
    public TransactionController(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping("/createpayment")
    public void createPayment(@RequestBody Transaction transaction) {
        jmsTemplate.convertAndSend("transaction.queue", transaction);
        log.info("Transaction succeed, sum:" + transaction.getAmount() + " " + transaction.getCurrency());
    }
}

