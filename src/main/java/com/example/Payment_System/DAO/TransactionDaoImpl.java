package com.example.Payment_System.DAO;

import com.example.Payment_System.Model.Transaction;
import com.example.Payment_System.Model.TransactionEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class TransactionDaoImpl implements TransactionDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransactionDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void createPayment(Transaction transaction, List<TransactionEntry> entries) {
        // 1. Вставка транзакции и получение ID
        Integer transactionId = jdbcTemplate.queryForObject(
                "INSERT INTO transactions (" +
                        "amount, currency, date, " +
                        "bic_sender, bic_receiver, " +  // Добавлены
                        "account_sender, account_receiver, " +
                        "type_id, comments" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING transaction_id",  // 9 параметров
                Integer.class,
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getDate() != null ? Timestamp.valueOf(transaction.getDate()) : null,
                transaction.getBic_sender(),    // Новые поля
                transaction.getBic_receiver(),  //
                transaction.getAccount_sender(),
                transaction.getAccount_receiver(),
                transaction.getType_id(),       // type_id вместо typeId
                transaction.getComments()
        );

        // 2. Пакетная вставка проводок
        jdbcTemplate.batchUpdate(
                "INSERT INTO transaction_entries (" +
                        "transactionId, account_number, " +
                        "amount, currency, entry_date" +  // entry_date вместо entryDate
                        ") VALUES (?, ?, ?, ?, ?)",
                entries.stream()
                        .map(e -> new Object[]{
                                transactionId,
                                e.getAccount_number(),  // account_number вместо accountNumber
                                e.getAmount(),
                                e.getCurrency(),
                                e.getEntry_date() != null ? Timestamp.valueOf(e.getEntry_date()) : null
                        })
                        .toList()
        );
    }

    @Override
    public BigDecimal calculateBalance(String accountNumber) {
        return jdbcTemplate.queryForObject(
                "SELECT current_balance + COALESCE(" +
                        "(SELECT SUM(amount) FROM transaction_entries WHERE account_number = ?), 0) " +
                        "FROM accounts WHERE account_number = ?",
                BigDecimal.class,
                accountNumber, accountNumber
        );
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return jdbcTemplate.query(
                "SELECT * FROM transactions",
                (rs, rowNum) -> {
                    Transaction t = new Transaction();
                    // Основные поля
                    t.setTransaction_id(rs.getInt("transaction_id"));
                    t.setAmount(rs.getBigDecimal("amount"));
                    t.setCurrency(rs.getString("currency"));

                    // Обработка даты (Timestamp → String)
                    Timestamp timestamp = rs.getTimestamp("date");
                    t.setDate(timestamp != null ?
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp) :
                            null);

                    // Поля отправителя/получателя
                    t.setBic_sender(rs.getString("bic_sender"));
                    t.setBic_receiver(rs.getString("bic_receiver"));
                    t.setAccount_sender(rs.getString("account_sender"));
                    t.setAccount_receiver(rs.getString("account_receiver"));

                    // Дополнительные поля
                    t.setType_id(rs.getInt("type_id"));
                    t.setComments(rs.getString("comments"));

                    // Загрузка связанных проводок
                    t.setEntries(getTransactionEntries(t.getTransaction_id()));
                    return t;
                }
        );
    }

    // Вспомогательный метод для загрузки проводок
    private List<TransactionEntry> getTransactionEntries(int transactionId) {
        return jdbcTemplate.query(
                "SELECT * FROM transaction_entries WHERE transactionId = ?",
                (rs, rowNum) -> {
                    TransactionEntry e = new TransactionEntry();
                    e.setId(rs.getInt("id"));
                    e.setTransactionId(rs.getInt("transactionId"));
                    e.setAccount_number(rs.getString("account_number"));
                    e.setAmount(rs.getBigDecimal("amount"));
                    e.setCurrency(rs.getString("currency"));

                    Timestamp entryTimestamp = rs.getTimestamp("entry_date");
                    e.setEntry_date(entryTimestamp != null ?
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entryTimestamp) :
                            null);

                    return e;
                },
                transactionId
        );
    }
//    @Override
//    public List<Transaction> getAllTransactions() {
//        return jdbcTemplate.query(
//                "SELECT * FROM transactions",
//                (rs, rowNum) -> new Transaction(
//                        rs.getInt("transaction_id"),
//                        rs.getBigDecimal("amount"),
//                        rs.getString("currency"),
//                        rs.getTimestamp("date").toLocalDateTime(),
//                        rs.getString("bic_sender"),
//                        rs.getString("bic_receiver"),
//                        rs.getString("account_sender"),
//                        rs.getString("account_receiver"),
//                        rs.getInt("type_id"),
//                        rs.getString("comments"),
//                        null
//                )
//        );
//    }

    @Override
    public BigDecimal getBalanceByAccount(String accountNumber) {
        return jdbcTemplate.queryForObject(
                "SELECT current_balance FROM accounts WHERE account_number = ?",
                BigDecimal.class,
                accountNumber
        );
    }
}

