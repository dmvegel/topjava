package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsDAO {
    void create(Meal meal);

    Meal get(int id);

    List<Meal> getAll();

    void update(Meal meal);

    void delete(int id);
}
