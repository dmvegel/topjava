package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void getUserWithMeals() {
        User expected = new User(UserTestData.user);
        expected.setMeals(MealTestData.meals);
        USER_WITH_MEALS_MATCHER.assertMatch(service.getUserWithMeals(USER_ID), expected);
    }

    @Test
    public void getUserWithEmptyMeals() {
        USER_WITH_MEALS_MATCHER.assertMatch(service.getUserWithMeals(GUEST_ID), UserTestData.guest);
    }

    @Test
    public void getNotExistingUserWithMeals() {
        USER_WITH_MEALS_MATCHER.assertMatch(service.getUserWithMeals(NOT_FOUND), null);
    }
}
