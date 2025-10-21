package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.javawebinar.topjava.model.AbstractBaseEntity;

public abstract class AbstractJdbcRepository<T extends AbstractBaseEntity> {
    protected final JdbcTemplate jdbcTemplate;
    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected final SimpleJdbcInsert simpleJdbcInsert;
    protected final BeanPropertyRowMapper<T> rowMapper;

    protected AbstractJdbcRepository(JdbcTemplate jdbcTemplate,
                                     NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                     String tableName,
                                     Class<T> mappingClass) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(tableName)
                .usingGeneratedKeyColumns("id");
        this.rowMapper = BeanPropertyRowMapper.newInstance(mappingClass);
    }

    protected T save(T entity, MapSqlParameterSource params) {
        if (entity.isNew()) {
            int id = simpleJdbcInsert.executeAndReturnKey(params).intValue();
            entity.setId(id);
        } else if (update(entity, params) == 0) {
            return null;
        }
        return entity;
    }

    protected abstract int update(T entity, MapSqlParameterSource params);
}
