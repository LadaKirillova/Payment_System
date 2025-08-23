package com.example.Payment_System.Configurations.Security;

import com.example.Payment_System.Configurations.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    // Сервис для работы с JWT (проверка токенов)
    private final JwtUtils jwtUtils;
    // Сервис для загрузки пользователей из БД
    private final UserDetailsService userDetailsService;

    // Конструктор (Spring сам передаст эти объекты)
    public JwtTokenFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        // 1. Извлекаем JWT токен из заголовка Authorization
        String token = getTokenFromHeader(request);

        // 2. Если токен присутствует и валиден
        if (token != null && jwtUtils.validateToken(token)) {
            // 3. Извлекаем имя пользователя из токена
            String username = jwtUtils.getUsernameFromToken(token);

            // 4. Проверяем, что пользователь еще не аутентифицирован в текущем контексте
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 5. Загружаем данные пользователя из базы данных
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 6. Создаем объект аутентификации Spring Security
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails,       // Основные данные пользователя
                        null,               // Учетные данные (пароль не нужен, так как уже аутентифицированы)
                        userDetails.getAuthorities() // Список ролей/привилегий
                );

                // 7. Сохраняем объект аутентификации в контексте безопасности
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // 8. Передаем запрос дальше по цепочке фильтров
        chain.doFilter(request, response);
    }

    // Метод для извлечения токена из заголовка
    private String getTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Убираем "Bearer "
        }
        return null;
    }
}

//Что делает фильтр?
//
//Берёт токен из заголовка Authorization.
//
//Проверяет его через JwtUtils.
//
//Если токен валиден — достаёт логин пользователя.