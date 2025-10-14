package com.example.Payment_System.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxService outboxService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedDelay = 30000)
    public void publishUnsent() {
        try {
            List<Map<String, Object>> unsent = outboxService.findUnsent();
            if (unsent.isEmpty()) {
                log.debug("Нет новых событий в outbox");
                return;
            }

            unsent.stream().limit(10).forEach(row -> {
                Integer id = (Integer) row.get("id");
                String payload = row.get("payload").toString();

                try {
                    kafkaTemplate.send("receipts", payload);
                    outboxService.markAsSent(id);
                    log.info("Отправлено в Kafka (receipts): {}", payload);
                } catch (Exception e) {
                    log.error("Ошибка при отправке id={}: {}", id, e.getMessage());
                }
            });

        } catch (Exception e) {
            log.error("OutboxPublisher: ошибка при чтении outbox — {}", e.getMessage());
        }
    }
}
