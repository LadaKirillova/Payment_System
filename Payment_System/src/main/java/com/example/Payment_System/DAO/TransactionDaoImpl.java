package com.example.Payment_System.DAO;

import com.example.Payment_System.Configurations.Security.DatabaseRouter;
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

    private final DatabaseRouter databaseRouter;

    @Autowired
    public TransactionDaoImpl(DatabaseRouter databaseRouter) {
        this.databaseRouter = databaseRouter;
    }

    @Override
    @Transactional
    public void createPayment(Transaction transaction, List<TransactionEntry> entries) {
        // Определяем правильный JdbcTemplate на основе отправителя
        JdbcTemplate jdbcTemplate = databaseRouter.getTemplate(transaction.getAccount_sender());

        // Проверяем, что получатель того же типа (юр/физ)
        JdbcTemplate receiverTemplate = databaseRouter.getTemplate(transaction.getAccount_receiver());
        if (jdbcTemplate != receiverTemplate) {
            throw new IllegalArgumentException("Cannot transfer between different account types");
        }

        // 1. Вставка транзакции и получение ID
        Integer transactionId = jdbcTemplate.queryForObject(
                "INSERT INTO transactions (" +
                        "amount, currency, date, " +
                        "bic_sender, bic_receiver, " +
                        "account_sender, account_receiver, " +
                        "type_id, comments" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING transaction_id",
                Integer.class,
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getDate() != null ? Timestamp.valueOf(transaction.getDate()) : null,
                transaction.getBic_sender(),
                transaction.getBic_receiver(),
                transaction.getAccount_sender(),
                transaction.getAccount_receiver(),
                transaction.getType_id(),
                transaction.getComments()
        );

        // 2. Пакетная вставка проводок
        jdbcTemplate.batchUpdate(
                "INSERT INTO transaction_entries (" +
                        "transaction_id, account_number, " +
                        "amount, currency, entry_date" +
                        ") VALUES (?, ?, ?, ?, ?)",
                entries.stream()
                        .map(e -> new Object[]{
                                transactionId,
                                e.getAccount_number(),
                                e.getAmount(),
                                e.getCurrency(),
                                e.getEntry_date() != null ? Timestamp.valueOf(e.getEntry_date()) : null
                        })
                        .toList()
        );
    }

    @Override
    public BigDecimal calculateBalance(String accountNumber) {
        JdbcTemplate jdbcTemplate = databaseRouter.getTemplate(accountNumber);
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
        // Получаем транзакции из обеих БД и объединяем
        JdbcTemplate legalTemplate = databaseRouter.getTemplateByType("LEGAL");
        JdbcTemplate individualTemplate = databaseRouter.getTemplateByType("INDIVIDUAL");

        List<Transaction> legalTransactions = legalTemplate.query(
                "SELECT * FROM transactions",
                this::mapTransaction
        );

        List<Transaction> individualTransactions = individualTemplate.query(
                "SELECT * FROM transactions",
                this::mapTransaction
        );

        legalTransactions.addAll(individualTransactions);
        return legalTransactions;
    }

    private Transaction mapTransaction(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
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
        t.setEntries(getTransactionEntries(t.getTransaction_id(),
                databaseRouter.getTemplate(t.getAccount_sender())));
        return t;
    }

    // Вспомогательный метод для загрузки проводок
    private List<TransactionEntry> getTransactionEntries(int transactionId, JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query(
                "SELECT * FROM transaction_entries WHERE transaction_id = ?",
                (rs, rowNum) -> {
                    TransactionEntry e = new TransactionEntry();
                    e.setId(rs.getInt("id"));
                    e.setTransactionId(rs.getInt("transaction_id"));
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

    @Override
    public BigDecimal getBalanceByAccount(String accountNumber) {
        JdbcTemplate jdbcTemplate = databaseRouter.getTemplate(accountNumber);
        return jdbcTemplate.queryForObject(
                "SELECT current_balance FROM accounts WHERE account_number = ?",
                BigDecimal.class,
                accountNumber
        );
    }
}