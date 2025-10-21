package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_1_ID = START_SEQ + 3;
    public static final int USER_MEAL_2_ID = START_SEQ + 4;
    public static final int USER_MEAL_3_ID = START_SEQ + 5;
    public static final int USER_MEAL_4_ID = START_SEQ + 6;
    public static final int USER_MEAL_5_ID = START_SEQ + 7;
    public static final int USER_MEAL_6_ID = START_SEQ + 8;
    public static final int USER_MEAL_7_ID = START_SEQ + 9;

    public static final int ADMIN_MEAL_1_ID = START_SEQ + 10;
    public static final int ADMIN_MEAL_2_ID = START_SEQ + 11;
    public static final int ADMIN_MEAL_3_ID = START_SEQ + 12;
    public static final int ADMIN_MEAL_4_ID = START_SEQ + 13;
    public static final int ADMIN_MEAL_5_ID = START_SEQ + 14;
    public static final int ADMIN_MEAL_6_ID = START_SEQ + 15;
    public static final int ADMIN_MEAL_7_ID = START_SEQ + 16;

    public static final LocalDateTime DEFAULT_TIME = LocalDateTime.of(1970, 1, 1, 0, 0, 0);

    public static final int NOT_FOUND = 999999;

    public static final Meal user_Meal_1 = new Meal(USER_MEAL_1_ID, LocalDateTime.of(2020, 1, 30, 10, 0), "Завтрак", 500);
    public static final Meal user_Meal_2 = new Meal(USER_MEAL_2_ID, LocalDateTime.of(2020, 1, 30, 13, 0), "Обед", 1000);
    public static final Meal user_Meal_3 = new Meal(USER_MEAL_3_ID, LocalDateTime.of(2020, 1, 30, 20, 0), "Ужин", 500);
    public static final Meal user_Meal_4 = new Meal(USER_MEAL_4_ID, LocalDateTime.of(2020, 1, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal user_Meal_5 = new Meal(USER_MEAL_5_ID, LocalDateTime.of(2020, 1, 31, 10, 0), "Завтрак", 1000);
    public static final Meal user_Meal_6 = new Meal(USER_MEAL_6_ID, LocalDateTime.of(2020, 1, 31, 13, 0), "Обед", 500);
    public static final Meal user_Meal_7 = new Meal(USER_MEAL_7_ID, LocalDateTime.of(2020, 1, 31, 20, 0), "Ужин", 410);

    public static final Meal admin_Meal_1 = new Meal(ADMIN_MEAL_1_ID, LocalDateTime.of(2020, 1, 30, 10, 0), "Завтрак админа", 500);
    public static final Meal admin_Meal_2 = new Meal(ADMIN_MEAL_2_ID, LocalDateTime.of(2020, 1, 30, 13, 0), "Обед админа", 1000);
    public static final Meal admin_Meal_3 = new Meal(ADMIN_MEAL_3_ID, LocalDateTime.of(2020, 1, 30, 20, 0), "Ужин админа", 500);
    public static final Meal admin_Meal_4 = new Meal(ADMIN_MEAL_4_ID, LocalDateTime.of(2020, 1, 31, 0, 0), "Еда на граничное значение админа", 100);
    public static final Meal admin_Meal_5 = new Meal(ADMIN_MEAL_5_ID, LocalDateTime.of(2020, 1, 31, 10, 0), "Завтрак админа", 1000);
    public static final Meal admin_Meal_6 = new Meal(ADMIN_MEAL_6_ID, LocalDateTime.of(2020, 1, 31, 13, 0), "Обед админа", 500);
    public static final Meal admin_Meal_7 = new Meal(ADMIN_MEAL_7_ID, LocalDateTime.of(2020, 1, 31, 20, 0), "Ужин админа", 410);

    public static Meal getNew() {
        return new Meal(null, DEFAULT_TIME, "", 0);
    }

    public static Meal createUpdated(Meal meal) {
        Meal updated = new Meal(meal);
        updated.setDateTime(DEFAULT_TIME);
        updated.setDescription("UpdatedDescription");
        updated.setCalories(999);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static <T> void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
