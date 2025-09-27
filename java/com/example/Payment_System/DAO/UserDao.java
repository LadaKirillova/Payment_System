package com.example.Payment_System.DAO;

import com.example.Payment_System.Model.User;

public interface UserDao {
    User findByLogin(String login); // Возвращает User или null
    void save(User user);
    boolean existsByLogin(String login);
}