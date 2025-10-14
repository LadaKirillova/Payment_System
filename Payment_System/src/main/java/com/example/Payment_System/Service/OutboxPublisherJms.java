package com.example.Payment_System.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.messaging.backend", havingValue = "artemis")
public class OutboxPublisherJms {

    private final OutboxService outboxService;
    private final JmsTemplate jmsTemplate;

    @Scheduled(fixedDelay = 5000)
    public void publishUnsent() {
        List<Map<String, Object>> unsent = outboxService.findUnsent();
        if (unsent.isEmpty()) return;

        for (Map<String, Object> row : unsent) {
            Integer id = (Integer) row.get("id");
            String payload = row.get("payload").toString();

            try {
                jmsTemplate.convertAndSend("receipt.queue", payload);
                outboxService.markAsSent(id);
                log.info("JMS Outbox: отправлено → {}", payload);
            } catch (Exception e) {
                log.error("JMS Outbox: ошибка при отправке id={}: {}", id, e.getMessage());
            }
        }
    }
}

