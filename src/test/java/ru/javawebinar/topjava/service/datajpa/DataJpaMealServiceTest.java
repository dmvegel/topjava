package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.MealTestData.MEAL_WITH_USER_MATCHER;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
    @Test
    public void getMealWithUser() {
        Meal expected = new Meal(MealTestData.meal1);
        expected.setUser(UserTestData.user);
        MEAL_WITH_USER_MATCHER.assertMatch(service.getMealWithUser(MEAL1_ID, USER_ID), expected);
    }

    @Test
    public void getMealForAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.getMealWithUser(MEAL1_ID, ADMIN_ID));
    }
}
