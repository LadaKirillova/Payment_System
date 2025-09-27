package com.example.Payment_System;

import com.example.Payment_System.Configurations.Security.DatabaseRouter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.initdata.enabled", havingValue = "true") // <- включаем сидер флагом
public class TestData {
    private final DatabaseRouter databaseRouter;

    public TestData(DatabaseRouter databaseRouter) {
        this.databaseRouter = databaseRouter;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            System.out.println("Начинаем инициализацию тестовых данных...");

            JdbcTemplate legalTemplate = databaseRouter.getTemplateByType("LEGAL");
            JdbcTemplate individualTemplate = databaseRouter.getTemplateByType("INDIVIDUAL");

            // Ждём готовности обеих БД (убирает "Failed to obtain JDBC Connection")
            waitReady(legalTemplate, "LEGAL");
            waitReady(individualTemplate, "INDIVIDUAL");

            String[][] testBics = {
                    {"12345678", "ПАО \"Банк ВТБ\""},
                    {"12348765", "АО \"Альфа-Банк\""},
                    {"BIC789", "Test Bank 789"},
                    {"BIC101", "Test Bank 101"},
                    {"BIC202", "Test Bank 202"},
                    {"BIC303", "Test Bank 303"}
            };

            for (String[] bic : testBics) {
                try {
                    int legalRows = legalTemplate.update(
                            "INSERT INTO payment_references (bic, bank_name) VALUES (?, ?) ON CONFLICT (bic) DO NOTHING",
                            bic[0], bic[1]);
                    int individualRows = individualTemplate.update(
                            "INSERT INTO payment_references (bic, bank_name) VALUES (?, ?) ON CONFLICT (bic) DO NOTHING",
                            bic[0], bic[1]);
                    if (legalRows > 0 || individualRows > 0) {
                        System.out.println("Создан BIC: " + bic[0]);
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка при создании BIC " + bic[0] + ": " + e.getMessage());
                }
            }

            String[][] testAccounts = {
                    {"456789", "100000.0", "USD"},
                    {"411000", "100000.0", "USD"},
                    {"499999", "100000.0", "USD"},
                    {"556789", "100000.0", "USD"},
                    {"511000", "100000.0", "USD"},
                    {"599999", "100000.0", "USD"}
            };

            for (String[] account : testAccounts) {
                try {
                    JdbcTemplate target = databaseRouter.getTemplate(account[0]);
                    int rows = target.update(
                            "INSERT INTO accounts (account_number, current_balance, currency) " +
                                    "VALUES (?, ?, ?) ON CONFLICT (account_number) DO NOTHING",
                            account[0], Double.parseDouble(account[1]), account[2]);
                    if (rows > 0) {
                        System.out.println("Создан аккаунт: " + account[0] +
                                " в " + (account[0].startsWith("4") ? "LEGAL" : "INDIVIDUAL") + " базе");
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка при создании аккаунта " + account[0] + ": " + e.getMessage());
                }
            }

            System.out.println("Инициализация тестовых данных завершена");
        } catch (Exception e) {
            System.out.println("Критическая ошибка при инициализации тестовых данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void waitReady(JdbcTemplate jt, String name) {
        for (int i = 0; i < 30; i++) {
            try {
                jt.execute("SELECT 1");
                return;
            } catch (Exception ignored) {
                try { Thread.sleep(1000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
        throw new IllegalStateException("DB " + name + " не доступна");
    }
}
