package com.example.Payment_System.Controller;

import com.example.Payment_System.Model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final JmsTemplate jmsTemplate;

    @Autowired
    public TransactionController(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping("/createpayment")
    public String createPayment(@RequestBody Transaction transaction) {
        jmsTemplate.convertAndSend("transaction.queue", transaction);
        return "Payment is in process";
    }
}