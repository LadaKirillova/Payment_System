package com.example.Payment_System.Controller;//package com.example.Payment_System.Controller;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/test")
//public class TestController {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public TestController(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @GetMapping("/db")
//    public String testDbConnection() {
//        try {
//            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
//            return "Database connection OK! Result: " + result;
//        } catch (Exception e) {
//            return "Database connection FAILED: " + e.getMessage();
//        }
//    }
//}

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ArtemisTester implements CommandLineRunner {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void run(String... args) {
        System.out.println("Sending test JSON message...");
        jmsTemplate.convertAndSend("payments", Map.of(
                "amount", 100.00,
                "currency", "EUR",
                "accountSender", "TEST123",
                "accountReceiver", "TEST456"
        ));
    }
}