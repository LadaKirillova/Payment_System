package com.example.Payment_System.Controller;

import com.example.Payment_System.Configurations.JwtUtils;
import com.example.Payment_System.Configurations.Security.RefreshTokenService;
import com.example.Payment_System.Model.User;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;


    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/api/auth/login")
    public String login(@RequestBody AuthRequest request) {
        // Создаем объект аутентификации
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );

        // Получаем аутентифицированного пользователя
        User user = (User) authentication.getPrincipal();

        // Генерируем JWT токен
        return jwtUtils.generateToken(user.getUsername(), user.getRole());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refresh_token");
        String username = request.get("username");

        // 1. Проверяем refresh-token из Redis
        if (!refreshTokenService.validateRefreshToken(username, refreshToken)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid refresh token"));
        }

        // 2. Если всё ок — генерируем новый access token
        String newAccessToken = jwtUtils.generateToken(username, "USER");

        return ResponseEntity.ok(Map.of(
                "access_token", newAccessToken
        ));
    }

//    Клиент присылает username + refresh_token
//    Мы проверяем refresh в Redis
//    Если совпадает → выдаём новый access-token
//    Refresh остаётся прежним


//    @GetMapping("/test-auth")
//    public String testAuth(@AuthenticationPrincipal User user) {
//        return "Authentication works! User: " + user.getLogin() + ", Role: " + user.getRole();
//    }

    @GetMapping("/test-auth")
    public String testAuth(@AuthenticationPrincipal User user) {
        if (user == null) {
            return "Authentication failed - user is null. JWT valid but user not loaded.";
        }
        return "Authentication works! User: " + user.getLogin() + ", Role: " + user.getRole();
    }

    // метод для работы через Tyk
    @GetMapping("/api/test-auth")
    public String testAuthApi(@AuthenticationPrincipal User user) {
        if (user == null) {
            return "API: Authentication failed - user is null. JWT valid but user not loaded.";
        }
        return "API: Authentication works! User: " + user.getLogin() + ", Role: " + user.getRole();
    }

    // DTO для запроса
    @Data
    static class AuthRequest {
        private String login;
        private String password;
    }
}