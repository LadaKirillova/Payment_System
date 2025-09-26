package com.example.Payment_System.DAO;

import com.example.Payment_System.Model.Transaction;
import com.example.Payment_System.Model.TransactionEntry;

import java.math.BigDecimal;
import java.util.List;


public interface TransactionDao {
//    void createTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
    BigDecimal getBalanceByAccount(String accountNumber);

    void createPayment(Transaction transaction, List<TransactionEntry> entries);
//    List<TransactionEntry> findEntriesByAccount(String accountNumber);
    BigDecimal calculateBalance(String accountNumber);
}

