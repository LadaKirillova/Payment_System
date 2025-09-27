package com.example.Payment_System.Service;

import com.example.Payment_System.DAO.TransactionDao;
import com.example.Payment_System.Model.Transaction;
import com.example.Payment_System.Model.TransactionEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionDao transactionDao;

    @Autowired
    public TransactionService(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

//    public void createTransaction(Transaction transaction) {
//        transactionDao.createTransaction(transaction);
//    }

    public List<Transaction> getAllTransactions() {
        return transactionDao.getAllTransactions();
    }

    public BigDecimal getBalanceByAccount(String accountNumber) {
        return transactionDao.getBalanceByAccount(accountNumber);
    }

    @Transactional
    public void makePayment(Transaction transaction, List<TransactionEntry> entries) {
        transactionDao.createPayment(transaction, entries);
    }

    public BigDecimal calculateBalance(String accountNumber) {
        return transactionDao.calculateBalance(accountNumber);
    }


}

