package com.example.Payment_System;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private BigDecimal amount;
    private String currency;
    private LocalDateTime date;
    private String bicSender;
    private String bicReceiver;
    private String accountSender;
    private String accountReceiver;
    private Integer typeId;
    private String comments;

    // Геттеры и сеттеры (можно сгенерировать автоматически в IDE)
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    // ... остальные геттеры и сеттеры
}