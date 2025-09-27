package com.example.Payment_System.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
public class TransactionEntry {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("transactionId")
    private Integer transactionId;

    @JsonProperty("accountNumber")
    private String account_number;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("entryDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String entry_date;
}

