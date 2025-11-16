package com.example.Payment_System.Controller;

import com.example.Payment_System.Model.Transaction;
import com.example.Payment_System.Service.TransactionService;
import com.example.Payment_System.TransactionProcessor.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GraphQLController {

    private final TransactionService transactionService;
    private final MessageSender sender;

    // ===== ПРОСТЕЙШИЕ QUERIES =====

    @QueryMapping
    public String test() {
        return "GraphQL ok";
    }

    @QueryMapping
    public List<Transaction> transactions() {
        return transactionService.getAllTransactions();
    }

    @QueryMapping
    public BigDecimal balance(@Argument String accountNumber) {
        // Простейший запрос баланса
        return transactionService.getBalanceByAccount(accountNumber);
    }

    // ===== ПРОСТЕЙШАЯ MUTATION =====

    @MutationMapping
    public String createPayment(
            @Argument String accountFrom,
            @Argument String accountTo,
            @Argument Double amount,
            @Argument String currency) {

        try {
            // Создаем транзакцию как в вашем REST контроллере
            Transaction transaction = new Transaction();
            transaction.setAccount_sender(accountFrom);
            transaction.setAccount_receiver(accountTo);
            transaction.setAmount(BigDecimal.valueOf(amount));
            transaction.setCurrency(currency != null ? currency : "USD");
            transaction.setType_id(1);
            transaction.setComments("Payment via GraphQL");

            // Отправляем в систему обработки (как в REST)
            sender.send(transaction);

            return "Платеж создан успешно!";

        } catch (Exception e) {
            return "Ошибка: " + e.getMessage();
        }
    }
}