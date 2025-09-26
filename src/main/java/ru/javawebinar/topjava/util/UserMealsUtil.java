package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),

                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 2, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 2, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 2, 20, 0), "Ужин", 410),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 2, 0, 0), "Еда на граничное значение", 100),

                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 3, 11, 0), "Завтрак", 10000),

                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 4, 11, 59), "Завтрак", 100)
        );

        System.out.println(filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(filteredByCyclesOptional(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(filteredByStreamsOptional(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayCalories = new HashMap<>();
        List<UserMealWithExcess> result = new ArrayList<>();

        for (UserMeal meal : meals) {
            dayCalories.put(meal.getDateTime().toLocalDate(), dayCalories.getOrDefault(meal.getDateTime().toLocalDate(), 0) + meal.getCalories());
        }


        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        dayCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayCalories = meals.stream().collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), dayCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .toList();
    }

    public static List<UserMealWithExcess> filteredByCyclesOptional(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayCalories = new HashMap<>();
        Map<LocalDate, UserMealWithExcess.ExcessHolder> dayExcesses = new HashMap<>();
        List<UserMealWithExcess> result = new ArrayList<>();

        for (UserMeal meal : meals) {
            int dayCalorie = dayCalories.getOrDefault(meal.getDateTime().toLocalDate(), 0) + meal.getCalories();

            dayCalories.put(meal.getDateTime().toLocalDate(), dayCalorie);
            UserMealWithExcess.ExcessHolder excessHolder = dayExcesses.computeIfAbsent(meal.getDateTime().toLocalDate(), k -> new UserMealWithExcess.ExcessHolder(false));

            if (dayCalorie > caloriesPerDay && !excessHolder.getExcess()) {
                excessHolder.setExcess(true);
            }

            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        excessHolder));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreamsOptional(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(Collector.of(
                () -> new Object() {
                    final Map<LocalDate, Integer> dayCalories = new HashMap<>();
                    final Map<LocalDate, UserMealWithExcess.ExcessHolder> dayExcesses = new HashMap<>();
                    final List<UserMealWithExcess> result = new ArrayList<>();
                },
                (acc, meal) -> {
                    LocalDate date = meal.getDateTime().toLocalDate();

                    int updatedCalories = acc.dayCalories.getOrDefault(date, 0) + meal.getCalories();
                    acc.dayCalories.put(date, updatedCalories);

                    UserMealWithExcess.ExcessHolder holder =
                            acc.dayExcesses.computeIfAbsent(date, d -> new UserMealWithExcess.ExcessHolder(false));

                    if (updatedCalories > caloriesPerDay) {
                        holder.setExcess(true);
                    }

                    if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                        acc.result.add(new UserMealWithExcess(
                                meal.getDateTime(),
                                meal.getDescription(),
                                meal.getCalories(),
                                holder));
                    }
                },
                (a, b) -> {
                    throw new UnsupportedOperationException("Not supported");
                },
                acc -> acc.result
        ));
    }
}
