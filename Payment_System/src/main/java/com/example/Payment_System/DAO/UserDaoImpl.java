package com.example.Payment_System.DAO;

import com.example.Payment_System.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Класс для преобразования строки БД в объект User
    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            return user;
        }
    }

    @Override
    public User findByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), login);
        } catch (Exception e) {
            return null; // Если пользователь не найден
        }
    }
    //Метод queryForObject в Spring's JdbcTemplate используется для выполнения SQL-запроса и возврата одного объекта, соответствующего результату запроса. Он предназначен для ситуаций, когда запрос должен вернуть только одну строку или одно значение

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (login, password, role) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getLogin(), user.getPassword(), user.getRole());
    }

    @Override
    public boolean existsByLogin(String login) {
        String sql = "SELECT COUNT(*) FROM users WHERE login = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, login);
        return count != null && count > 0;
    }

}
