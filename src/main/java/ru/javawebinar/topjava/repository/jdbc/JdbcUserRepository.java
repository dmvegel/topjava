package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import javax.validation.Validator;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final ResultSetExtractor<Map<Integer, User>> USER_MAPPER = rs -> {
        Map<Integer, User> data = new LinkedHashMap<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String password = rs.getString("password");
            Date registered = rs.getTimestamp("registered");
            boolean enabled = rs.getBoolean("enabled");
            int caloriesPerDay = rs.getInt("calories_per_day");

            User user = new User(id, name, email, password, caloriesPerDay, enabled, registered, new ArrayList<>());
            data.putIfAbsent(id, user);

            Integer userId = rs.getInt("user_id");
            String roleLine = rs.getString("role");
            if (userId != 0 && roleLine != null && !roleLine.isBlank()) {
                data.get(userId).getRoles().add(Role.valueOf(roleLine));
            }
        }
        return data;
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate,
                              NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              Validator validator) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validator = validator;
    }

    @Override
    @Transactional
    @Modifying
    public User save(User user) {
        ValidationUtil.validate(user, validator);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.getId());
        }
        batchUpdate(new ArrayList<>(user.getRoles()), user.getId());
        return user;
    }

    @Override
    @Transactional
    @Modifying
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        Map<Integer, User> users = jdbcTemplate.query(
                "SELECT * FROM users LEFT JOIN user_role ur ON users.id = ur.user_id WHERE id=?", USER_MAPPER, id);
        return Objects.requireNonNull(users).get(id);
    }

    @Override
    public User getByEmail(String email) {
        Map<Integer, User> users = jdbcTemplate.query(
                "SELECT * FROM users LEFT JOIN user_role ur ON users.id = ur.user_id  WHERE email=?", USER_MAPPER, email);
        return Objects.requireNonNull(DataAccessUtils.singleResult(Objects.requireNonNull(users).entrySet())).getValue();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(Objects.requireNonNull(jdbcTemplate.query(
                "SELECT * FROM users LEFT JOIN user_role ur ON users.id = ur.user_id ORDER BY name, email",
                USER_MAPPER)).values());
    }

    private void batchUpdate(List<Role> roles, Integer userId) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO user_role (user_id, role) VALUES(?,?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, userId);
                        ps.setString(2, roles.get(i).name());
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }
                });
    }
}
