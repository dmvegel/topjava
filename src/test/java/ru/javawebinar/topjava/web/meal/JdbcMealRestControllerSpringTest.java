package ru.javawebinar.topjava.web.meal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;

@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRestControllerSpringTest {
    @Autowired
    private MealRestController controller;

    @Test
    public void getAnotherMeal() {
        assertThrows(NotFoundException.class, () -> controller.get(ADMIN_MEAL_1_ID));
    }

    @Test
    public void updateAnotherMeal() {
        assertThrows(IllegalArgumentException.class, () -> controller.update(user_Meal_1, ADMIN_MEAL_1_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }

    @Test
    public void deleteAnotherMeal() {
        assertThrows(NotFoundException.class, () -> controller.delete(ADMIN_MEAL_1_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> controller.get(NOT_FOUND));
    }

    @Test
    public void updateNotFound() {
        Meal meal = MealTestData.createUpdated(MealTestData.user_Meal_1);
        meal.setId(NOT_FOUND);
        assertThrows(NotFoundException.class, () -> controller.update(meal, NOT_FOUND));
    }

    @Test
    public void get() {
        Meal meal = controller.get(USER_MEAL_1_ID);
        assertMatch(meal, user_Meal_1);
    }

    @Test
    public void delete() {
        controller.delete(USER_MEAL_1_ID);
        Assert.assertThrows(NotFoundException.class, () -> controller.get(USER_MEAL_1_ID));
    }

    @Test
    public void update() {
        Meal updated = MealTestData.createUpdated(MealTestData.user_Meal_1);
        controller.update(updated, USER_MEAL_1_ID);
        assertMatch(controller.get(USER_MEAL_1_ID), updated);

        assertThrows(DuplicateKeyException.class, () -> controller.update(MealTestData.createUpdated(MealTestData.admin_Meal_1), ADMIN_MEAL_1_ID));
    }

    @Test
    public void create() {
        Meal created = controller.create(MealTestData.getNew());
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(controller.get(newId), newMeal);
    }

    @Test
    public void getAll() {
        List<MealTo> all = controller.getAll();
        assertMatch(all, MealsUtil.getTos(
                Arrays.asList(user_Meal_7, user_Meal_6, user_Meal_5, user_Meal_4, user_Meal_3, user_Meal_2, user_Meal_1),
                SecurityUtil.authUserCaloriesPerDay()));
    }

    @Test
    public void getBetween() {
        LocalDate date = LocalDate.parse("2020-01-31");
        LocalTime timeLeft = LocalTime.parse("10:00");
        LocalTime timeRight = LocalTime.parse("15:00");
        List<MealTo> mealsSameDay = new ArrayList<>();
        mealsSameDay.addAll(controller.getBetween(date, timeRight, date, null));
        mealsSameDay.addAll(controller.getBetween(date, timeLeft, date, timeRight));
        mealsSameDay.addAll(controller.getBetween(date, null, date, timeLeft));
        assertMatch(mealsSameDay, MealsUtil.getTos(
                Arrays.asList(user_Meal_7, user_Meal_6, user_Meal_5, user_Meal_4),
                SecurityUtil.authUserCaloriesPerDay()));
    }

    @Test
    public void duplicateDateTimeCreate() {
        Meal meal = MealTestData.getNew();
        meal.setDateTime(user_Meal_1.getDateTime());
        assertThrows(DuplicateKeyException.class, () -> controller.create(meal));
    }
}
