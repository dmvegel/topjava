package ru.javawebinar.topjava.service.datajpa;

import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static java.util.Collections.emptyList;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class UserServiceDataJpaTest extends AbstractUserServiceTest {
    @Test
    public void getUserWithMeals() {
        User result = service.getUserWithMeals(USER_ID);
        USER_MATCHER.assertMatch((User) Hibernate.unproxy(result), UserTestData.user);
        MEAL_MATCHER.assertMatch(result.getMeals(), MealTestData.meals);
    }

    @Test
    public void getUserWithEmptyMeals() {
        User result = service.getUserWithMeals(GUEST_ID);
        USER_MATCHER.assertMatch((User) Hibernate.unproxy(result), UserTestData.guest);
        MEAL_MATCHER.assertMatch(result.getMeals(), emptyList());
    }
}
