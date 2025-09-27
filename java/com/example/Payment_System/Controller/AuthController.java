package com.example.Payment_System.Controller;

import com.example.Payment_System.Configurations.JwtUtils;
import com.example.Payment_System.Model.User;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
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

    @GetMapping("/test-auth")
    public String testAuth(@AuthenticationPrincipal User user) {
        return "Authentication works! User: " + user.getLogin() + ", Role: " + user.getRole();
    }

    // DTO для запроса
    @Data
    static class AuthRequest {
        private String login;
        private String password;
    }
}