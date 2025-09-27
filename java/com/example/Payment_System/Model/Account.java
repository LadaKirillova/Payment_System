package com.example.Payment_System.Model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Account {
    private String accountNumber;
    private BigDecimal currentBalance;
    private String currency;
    private LocalDateTime openedDate;
    private String status;
}