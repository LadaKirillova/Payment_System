package com.example.Payment_System.DAO;

import com.example.Payment_System.Model.User;
import com.example.Payment_System.Configurations.Security.DatabaseRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDaoImpl implements UserDao {
    private final DatabaseRouter databaseRouter;

    @Autowired
    public UserDaoImpl(DatabaseRouter databaseRouter) {
        this.databaseRouter = databaseRouter;
    }

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
        // Пример: выбираем БД по первой букве логина
        JdbcTemplate jdbcTemplate = login.startsWith("L")
                ? databaseRouter.getTemplateByType("LEGAL")
                : databaseRouter.getTemplateByType("INDIVIDUAL");

        String sql = "SELECT * FROM users WHERE login = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), login);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void save(User user) {
        JdbcTemplate jdbcTemplate = "LEGAL".equalsIgnoreCase(user.getRole())
                ? databaseRouter.getTemplateByType("LEGAL")
                : databaseRouter.getTemplateByType("INDIVIDUAL");

        String sql = "INSERT INTO users (login, password, role) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getLogin(), user.getPassword(), user.getRole());
    }

    @Override
    public boolean existsByLogin(String login) {
        JdbcTemplate jdbcTemplate = login.startsWith("L")
                ? databaseRouter.getTemplateByType("LEGAL")
                : databaseRouter.getTemplateByType("INDIVIDUAL");

        String sql = "SELECT COUNT(*) FROM users WHERE login = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, login);
        return count != null && count > 0;
    }
}
