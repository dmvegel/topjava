package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> mealsMapByUserId = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.createMeals().forEach(meal -> save(1, meal));
        MealsUtil.createMeals().forEach(meal -> save(2, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Map<Integer, Meal> mealsMap = getMealsMap(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealsMap.put(meal.getId(), meal);
            return meal;
        }
        return mealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        return getMealsMap(userId).remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        return getMealsMap(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return sorted(getMealsMap(userId).values().stream());
    }

    @Override
    public List<Meal> getInRange(int userId, LocalDate start, LocalDate end) {
        return sorted(getMealsMap(userId).values().stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(),
                        start,
                        end == LocalDate.MAX ? LocalDate.MAX : end.plusDays(1))));
    }

    private List<Meal> sorted(Stream<Meal> mealStream) {
        return mealStream.sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getMealsMap(int userId) {
        return mealsMapByUserId.computeIfAbsent(userId, id -> new ConcurrentHashMap<>());
    }
}

