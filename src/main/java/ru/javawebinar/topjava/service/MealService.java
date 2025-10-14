package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(int userId, Meal meal) {
        return repository.save(userId, meal);
    }

    public void delete(int userId, int id) {
        checkNotFound(repository.delete(userId, id), id);
    }

    public Meal get(int userId, int id) {
        return checkNotFound(repository.get(userId, id), id);
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        return MealsUtil.getTos(repository.getAll(userId), caloriesPerDay);
    }

    public List<MealTo> getInRange(int userId,
                                   int caloriesPerDay,
                                   LocalDate dateFrom,
                                   LocalDate dateTo,
                                   LocalTime timeFrom,
                                   LocalTime timeTo) {
        return MealsUtil.getFilteredTos(repository.getInRange(userId, dateFrom, dateTo), caloriesPerDay, timeFrom, timeTo);
    }

    public Meal update(int userId, Meal meal) {
        return checkNotFound(repository.save(userId, meal), meal.getId());
    }

}