package com.example.Payment_System.Configurations.Security;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate redisTemplate;

    private final String REFRESH_PREFIX = "refresh_";
    private final Duration REFRESH_TTL = Duration.ofDays(30); // срок жизни refresh

    public void saveRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue()
                .set(REFRESH_PREFIX + username, refreshToken, REFRESH_TTL);
    }

    public boolean validateRefreshToken(String username, String refreshToken) {
        String saved = redisTemplate.opsForValue().get(REFRESH_PREFIX + username);
        return saved != null && saved.equals(refreshToken);
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete(REFRESH_PREFIX + username);
    }
}

//хранилище refresh-токенов