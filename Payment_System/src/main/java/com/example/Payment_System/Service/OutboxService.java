package com.example.Payment_System.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final JdbcTemplate legalJdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void save(Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            legalJdbcTemplate.update(
                    "INSERT INTO outbox (payload) VALUES (CAST(? AS JSONB))",
                    json
            );
            log.info("💾 Outbox: сохранено {}", json);
        } catch (Exception e) {
            log.error("utbox: ошибка при сохранении — {}", e.getMessage());
        }
    }

    public List<Map<String, Object>> findUnsent() {
        return legalJdbcTemplate.queryForList(
                "SELECT * FROM outbox WHERE sent = false ORDER BY created_at"
        );
    }

    public void markAsSent(Integer id) {
        legalJdbcTemplate.update("UPDATE outbox SET sent = true WHERE id = ?", id);
        log.info("Outbox запись {} помечена как отправленная", id);
    }
}
