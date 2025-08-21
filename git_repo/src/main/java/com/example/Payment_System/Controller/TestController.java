package com.example.Payment_System.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final JmsTemplate jmsTemplate;

    @GetMapping("/send-test")
    public String sendTestMessage() {
        String testJson = """
        {
            "amount": 100.00,
            "currency": "EUR",
            "date": "2025-11-17 11:50:00",
            "bic_sender": "12348765",
            "bic_receiver": "12345678",
            "accountSender": "50817810000000012345",
            "accountReceiver": "40817810000000012345",
            "typeId": 1,
            "comments": "Test payment",
            "entries": [
                {
                    "accountNumber": "50817810000000012345",
                    "amount": -100.00,
                    "currency": "EUR",
                    "entryDate": "2025-11-17 11:50:00"
                }
            ]
        }""";

        jmsTemplate.convertAndSend("payments", testJson);
        return "Test message sent";
    }
}