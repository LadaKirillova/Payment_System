import java.net.*;
import java.io.*;
import java.util.Random;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class LoadGenerator {

    private static final String[] TEST_ACCOUNTS = {
            "456789", "411000", "499999", "556789", "511000", "599999"
    };

    // Разделяем аккаунты по типам
    private static final List<String> LEGAL_ACCOUNTS = new ArrayList<>();
    private static final List<String> INDIVIDUAL_ACCOUNTS = new ArrayList<>();

    static {
        // Инициализируем списки аккаунтов по типам
        for (String account : TEST_ACCOUNTS) {
            if (account.startsWith("4")) {
                LEGAL_ACCOUNTS.add(account);
            } else if (account.startsWith("5")) {
                INDIVIDUAL_ACCOUNTS.add(account);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Ждем 90 секунд для полного запуска приложений...");
        Thread.sleep(90000);

        // Заменяем проверку через API на дополнительную задержку
        System.out.println("Дополнительно ждем 30 секунд для создания тестовых аккаунтов...");
        Thread.sleep(30000);

        int totalRequests = 9;
        int threadCount = 3;
        int requestsPerThread = totalRequests / threadCount;

        System.out.println("Запускаем нагрузочный тест...");
        System.out.println("Всего запросов: " + totalRequests);
        System.out.println("Количество потоков: " + threadCount);
        System.out.println("Юридические аккаунты: " + LEGAL_ACCOUNTS);
        System.out.println("Физические аккаунты: " + INDIVIDUAL_ACCOUNTS);

        for (int i = 0; i < threadCount; i++) {
            MyThread thread = new MyThread(i + 1, requestsPerThread);
            thread.start();
            Thread.sleep(50);
        }

        System.out.println("Все потоки запущены. Тест завершится автоматически.");
    }

    static class MyThread extends Thread {
        private int threadNumber;
        private int requestsToSend;
        private SimpleDateFormat dateFormat;
        private Random random;

        public MyThread(int threadNumber, int requestsToSend) {
            this.threadNumber = threadNumber;
            this.requestsToSend = requestsToSend;
            this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            this.random = new Random();
        }

        public void run() {
            try {
                for (int j = 0; j < requestsToSend; j++) {
                    try {
                        URL url = new URL("http://payment-app:8080/api/createpayment");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(10000);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setDoOutput(true);

                        String dateStr = dateFormat.format(new Date());

                        String senderAccount = TEST_ACCOUNTS[random.nextInt(TEST_ACCOUNTS.length)];
                        String receiverAccount;

                        // Выбираем получателя того же типа, что и отправитель
                        if (senderAccount.startsWith("4")) {
                            // Юридические счета
                            do {
                                receiverAccount = LEGAL_ACCOUNTS.get(random.nextInt(LEGAL_ACCOUNTS.size()));
                            } while (receiverAccount.equals(senderAccount));
                        } else {
                            // Физические счета
                            do {
                                receiverAccount = INDIVIDUAL_ACCOUNTS.get(random.nextInt(INDIVIDUAL_ACCOUNTS.size()));
                            } while (receiverAccount.equals(senderAccount));
                        }

                        // Используем существующие BIC-коды из базы данных
                        String senderBic = "12345678"; // ПАО "Банк ВТБ"
                        String receiverBic = "12348765"; // АО "Альфа-Банк"

                        String json = String.format(
                                "{\"accountFrom\":\"%s\"," +
                                        "\"accountTo\":\"%s\"," +
                                        "\"amount\":%.2f," +
                                        "\"currency\":\"USD\"," +
                                        "\"date\":\"%s\"," +
                                        "\"bic_sender\":\"%s\"," +
                                        "\"bic_receiver\":\"%s\"," +
                                        "\"account_sender\":\"%s\"," +
                                        "\"account_receiver\":\"%s\"," +
                                        "\"type_id\":1," +
                                        "\"comments\":\"Test transaction from thread %d\"}",
                                senderAccount,
                                receiverAccount,
                                random.nextDouble() * 1000,
                                dateStr,
                                senderBic,
                                receiverBic,
                                senderAccount,
                                receiverAccount,
                                threadNumber
                        );

                        try (OutputStream os = conn.getOutputStream()) {
                            byte[] input = json.getBytes("utf-8");
                            os.write(input, 0, input.length);
                        }

                        int responseCode = conn.getResponseCode();
                        System.out.println("Поток " + threadNumber + " - Ответ: " + responseCode);

                        Thread.sleep(100);
                    } catch (Exception e) {
                        System.out.println("Ошибка в потоке " + threadNumber + ": " + e.getMessage());
                    }
                }

                System.out.println("Поток " + threadNumber + " завершил работу.");

            } catch (Exception e) {
                System.out.println("Общая ошибка в потоке " + threadNumber + ": " + e.getMessage());
            }
        }
    }
}