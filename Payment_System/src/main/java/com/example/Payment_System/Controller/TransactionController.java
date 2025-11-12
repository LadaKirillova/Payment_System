package com.example.Payment_System.Controller;

import com.example.Payment_System.Model.Transaction;
import com.example.Payment_System.TransactionProcessor.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
// Для Артемис
//@Slf4j
//@RestController
//@RequestMapping("/api")
//public class TransactionController {
//
//    private final JmsTemplate jmsTemplate;
//
//    @Autowired
//    public TransactionController(JmsTemplate jmsTemplate) {
//        this.jmsTemplate = jmsTemplate;
//    }
//
//    @PostMapping("/createpayment")
//    public void createPayment(@RequestBody Transaction transaction) {
//        jmsTemplate.convertAndSend("transaction.queue", transaction);
//        log.info("Transaction succeed, sum:" + transaction.getAmount() + " " + transaction.getCurrency());
//    }
//
//}
//
// Для Кафки
//@Slf4j
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class TransactionController {
//    private final KafkaTemplate<String, Transaction> kafkaTemplate;
//
//    @PostMapping("/createpayment")
//    public void createPayment(@RequestBody Transaction transaction) {
//        kafkaTemplate.send("transactions", transaction);
//        log.info("Sent to Kafka: {} {}", transaction.getAmount(), transaction.getCurrency());
//    }
//}
// Контроллер без привязки к технологии
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {
    private final MessageSender sender;

    @PostMapping("/createpayment")
    public void createPayment(@RequestBody Transaction transaction) {
        sender.send(transaction);
        log.info("Payment enqueued: {} {}", transaction.getAmount(), transaction.getCurrency());
    }

    // endpoint for Tyk test
    @GetMapping("/tyktest")
    public String tyk() {
        return "tyk ok";
    }
}

