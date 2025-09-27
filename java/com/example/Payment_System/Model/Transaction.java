package com.example.Payment_System.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @JsonProperty("transaction_id")
    private int transaction_id;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String date;

    @JsonProperty("bic_sender")
    private String bic_sender;

    @JsonProperty("bic_receiver")
    private String bic_receiver;

    @JsonProperty("account_sender")
    private String account_sender;

    @JsonProperty("account_receiver")
    private String account_receiver;

    @JsonProperty("type_id")
    private int type_id;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("entries")
    private List<TransactionEntry> entries;
}