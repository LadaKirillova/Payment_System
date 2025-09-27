package com.example.Payment_System.Configurations;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Утилита для работы с JWT токенами
 */
@Component
public class JwtUtils {
    // Секретный ключ для подписи токенов (автоматически генерируется при старте приложения)
    // Используем алгоритм HS512, который требует ключ длиной минимум 512 бит
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Время жизни токена в миллисекундах (24 часа)
    private final long EXPIRATION_MS = 86400000;

    /**
     * Генерация JWT токена
     * @param username логин пользователя
     * @param role роль пользователя
     * @return сгенерированный токен в виде строки
     */


    /**
     * Инициализация после создания бина.
     * Преобразует строковый секрет в криптографический ключ.
     */

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)          // Устанавливаем subject (обычно логин/ID пользователя)
                .claim("role", role)           // Добавляем кастомное поле с ролью
                .setIssuedAt(new Date())       // Время создания токена
                .setExpiration(new Date(       // Время истечения токена
                        System.currentTimeMillis() + EXPIRATION_MS
                ))
                .signWith(SECRET_KEY)         // Подписываем токен сгенерированным ключом
                .compact();                    // Преобразуем в строку
    }

    /**
     * Проверка валидности токена
     * @param token токен для проверки
     * @return true если токен валиден, false если нет
     */
    public boolean validateToken(String token) {
        try {
            // Создаем парсер и проверяем токен
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)    // Устанавливаем ключ для проверки подписи
                    .build()
                    .parseClaimsJws(token);        // Пытаемся распарсить токен
            return true;                       // Если не было исключения - токен валиден
        } catch (JwtException | IllegalArgumentException e) {
            // Ловим исключения при:
            // - Неправильной подписи
            // - Просроченном токене
            // - Неправильном формате токена
            return false;
        }
    }

    /**
     * Извлечение имени пользователя из токена
     * @param token JWT токен
     * @return логин пользователя
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)     // Устанавливаем ключ проверки
                .build()
                .parseClaimsJws(token)         // Парсим токен
                .getBody()                    // Получаем тело токена (claims)
                .getSubject();                 // Извлекаем subject (логин пользователя)
    }
}