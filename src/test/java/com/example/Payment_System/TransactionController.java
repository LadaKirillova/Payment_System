package com.example.Payment_System;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    // Метод для создания новой транзакции (POST)
    @PostMapping
    public String createTransaction(@RequestBody Transaction transaction) {
        // Здесь будет логика сохранения транзакции
        return "Транзакция создана: " + transaction;
    }

    // Метод для получения списка транзакций (GET)
    @GetMapping
    public List<Transaction> getTransactions() {
        // Здесь будет логика получения списка транзакций
        return List.of(); // Пока возвращаем пустой список
    }

    // Метод для получения остатка на счете (GET)
    @GetMapping("/balance/{accountNumber}")
    public String getBalance(@PathVariable String accountNumber) {
        // Здесь будет логика получения баланса
        return "Баланс для счета " + accountNumber + ": 1000.00";
    }
}