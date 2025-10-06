package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsDao {
    Meal create(Meal meal);

    Meal get(int id);

    List<Meal> getAll();

    Meal update(Meal meal);

    void delete(int id);
}
