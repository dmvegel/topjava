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

    public static final int NOT_FOUND = 999999;

    public static final Meal user_Meal_1 = new Meal(USER_MEAL_1_ID, LocalDateTime.parse("2020-01-30T10:00:00"), "Завтрак", 500);
    public static final Meal user_Meal_2 = new Meal(USER_MEAL_2_ID, LocalDateTime.parse("2020-01-30T13:00:00"), "Обед", 1000);
    public static final Meal user_Meal_3 = new Meal(USER_MEAL_3_ID, LocalDateTime.parse("2020-01-30T20:00:00"), "Ужин", 500);
    public static final Meal user_Meal_4 = new Meal(USER_MEAL_4_ID, LocalDateTime.parse("2020-01-31T00:00:00"), "Еда на граничное значение", 100);
    public static final Meal user_Meal_5 = new Meal(USER_MEAL_5_ID, LocalDateTime.parse("2020-01-31T10:00:00"), "Завтрак", 1000);
    public static final Meal user_Meal_6 = new Meal(USER_MEAL_6_ID, LocalDateTime.parse("2020-01-31T13:00:00"), "Обед", 500);
    public static final Meal user_Meal_7 = new Meal(USER_MEAL_7_ID, LocalDateTime.parse("2020-01-31T20:00:00"), "Ужин", 410);

    public static final Meal admin_Meal_1 = new Meal(ADMIN_MEAL_1_ID, LocalDateTime.parse("2020-01-30T10:00:00"), "Завтрак админа", 500);
    public static final Meal admin_Meal_2 = new Meal(ADMIN_MEAL_2_ID, LocalDateTime.parse("2020-01-30T13:00:00"), "Обед админа", 1000);
    public static final Meal admin_Meal_3 = new Meal(ADMIN_MEAL_3_ID, LocalDateTime.parse("2020-01-30T20:00:00"), "Ужин админа", 500);
    public static final Meal admin_Meal_4 = new Meal(ADMIN_MEAL_4_ID, LocalDateTime.parse("2020-01-31T00:00:00"), "Еда на граничное значение админа", 100);
    public static final Meal admin_Meal_5 = new Meal(ADMIN_MEAL_5_ID, LocalDateTime.parse("2020-01-31T10:00:00"), "Завтрак админа", 1000);
    public static final Meal admin_Meal_6 = new Meal(ADMIN_MEAL_6_ID, LocalDateTime.parse("2020-01-31T13:00:00"), "Обед админа", 500);
    public static final Meal admin_Meal_7 = new Meal(ADMIN_MEAL_7_ID, LocalDateTime.parse("2020-01-31T20:00:00"), "Ужин админа", 410);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.MIN, "", 0);
    }

    public static Meal createUpdated(Meal meal) {
        Meal updated = new Meal(meal);
        updated.setDateTime(LocalDateTime.parse("2020-01-29T09:01:01"));
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
