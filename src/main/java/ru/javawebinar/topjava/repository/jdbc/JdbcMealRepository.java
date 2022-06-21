package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;

    final DateTimeFormatter DATETIMEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        String role = userId == 100001 ? "admin" : "user";
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("date_time", meal.getDateTime())
                .addValue("calories", meal.getCalories());

        if (meal.isNew()) {
            map.addValue("description", role + ": " + meal.getDescription());
            Number newKey = insertMeal.executeAndReturnKey(map);
            meal.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE meals SET date_time='" + meal.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")) +
                        "', description='" + meal.getDescription() +"', calories=:calories WHERE id=" + meal.getId(), map) == 0) {
            return null;
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (userId == jdbcTemplate.queryForObject("SELECT user_id FROM meals WHERE id=?", Integer.class, new Object[]{id})) {
            return jdbcTemplate.update("DELETE FROM meals WHERE id=?", id) != 0;
        }
        throw new RuntimeException();
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query("SELECT * FROM meals WHERE id=?", ROW_MAPPER, id);
        if (userId == jdbcTemplate.queryForObject("SELECT user_id FROM meals WHERE id=?", Integer.class, new Object[]{id})) {
            return DataAccessUtils.singleResult(meals);
        }
        throw new RuntimeException();
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id = " + userId + " ORDER BY date_time DESC", ROW_MAPPER);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE date_time > '" +
                startDateTime.format(DATETIMEFORMATTER) + "' AND date_time < '" + endDateTime.format(DATETIMEFORMATTER) +
                "' ORDER BY date_time DESC", ROW_MAPPER);
    }
}
