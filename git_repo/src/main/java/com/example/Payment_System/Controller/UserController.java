package com.example.Payment_System.Controller;

import com.example.Payment_System.Model.User;
import com.example.Payment_System.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для операций с пользователями.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Конструктор с внедрением зависимости UserService.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User userRequest) {
        // Создаем нового пользователя с минимально необходимыми данными
        User newUser = new User();
        newUser.setLogin(userRequest.getLogin());
        newUser.setPassword(userRequest.getPassword());

        userService.registerUser(newUser);
        return ResponseEntity.ok("User registered successfully");
    }
    /**
     * Внутренний класс DTO для регистрации пользователя.
     */
    static class UserRegistrationDto {
        private String login;
        private String password;

        // Геттеры и сеттеры
        public String getLogin() {
            return login;
        }
        public void setLogin(String login) {
            this.login = login;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }
}