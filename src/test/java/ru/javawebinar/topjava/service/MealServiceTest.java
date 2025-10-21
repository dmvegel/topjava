package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(USER_MEAL_1_ID, USER_ID);
        assertMatch(meal, user_Meal_1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(MealTestData.NOT_FOUND, USER_ID));
    }

    @Test
    public void delete() {
        mealService.delete(USER_MEAL_1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(USER_MEAL_1_ID, USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.delete(MealTestData.NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate localDate = LocalDate.of(2020, 1, 31);
        List<Meal> mealsSameDay = mealService.getBetweenInclusive(localDate, localDate, USER_ID);
        assertMatch(mealsSameDay, Arrays.asList(user_Meal_7, user_Meal_6, user_Meal_5, user_Meal_4));
    }

    @Test
    public void getBetweenSorted() {
        LocalDate localDateLeft = LocalDate.of(2020, 1, 30);
        LocalDate localDateRight = LocalDate.of(2020, 1, 31);
        List<Meal> meals = mealService.getBetweenInclusive(localDateLeft, localDateRight, USER_ID);
        assertMatch(meals, Arrays.asList(user_Meal_7, user_Meal_6, user_Meal_5, user_Meal_4, user_Meal_3, user_Meal_2, user_Meal_1));
    }

    @Test
    public void getAll() {
        List<Meal> all = mealService.getAll(USER_ID);
        assertMatch(all, Arrays.asList(user_Meal_7, user_Meal_6, user_Meal_5, user_Meal_4, user_Meal_3, user_Meal_2, user_Meal_1));
    }

    @Test
    public void update() {
        Meal updated = MealTestData.createUpdated(MealTestData.user_Meal_1);
        mealService.update(updated, USER_ID);
        assertMatch(mealService.get(USER_MEAL_1_ID, USER_ID), createUpdated(user_Meal_1));
    }

    @Test
    public void create() {
        Meal created = mealService.create(MealTestData.getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DuplicateKeyException.class, () ->
                mealService.create(new Meal(null, user_Meal_1.getDateTime(), "duplicate", 500), USER_ID));
    }

    @Test
    public void getAnotherUserMeal() {
        assertThrows(NotFoundException.class, () -> mealService.get(ADMIN_MEAL_1_ID, ADMIN_ID));
    }

    @Test
    public void deleteAnotherUserMeals() {
        assertThrows(NotFoundException.class, () -> mealService.delete(ADMIN_MEAL_1_ID, ADMIN_ID));
    }

    @Test
    public void updateAnotherUserMeals() {
        assertThrows(NotFoundException.class, () -> mealService.update(admin_Meal_1, ADMIN_ID));
    }
}