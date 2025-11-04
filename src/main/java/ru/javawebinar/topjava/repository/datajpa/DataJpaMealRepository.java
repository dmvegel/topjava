package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DataJpaMealRepository implements MealRepository {
    public static final Sort SORT_BY_DATE_TIME = Sort.by(Sort.Direction.DESC, "dateTime");

    private final CrudMealRepository crudRepository;
    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudRepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        meal.setUser(crudUserRepository.getReferenceById(userId));
        if (!meal.isNew()) {
            if (crudRepository.getByIdAndUserId(meal.id(), userId) == null) {
                return null;
            }
        }
        return crudRepository.save(meal);
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return crudRepository.deleteByIdAndUserId(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.getByIdAndUserId(id, userId);
    }

    public List<Meal> getAll(int userId) {
        return crudRepository.getByUserId(userId, SORT_BY_DATE_TIME);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThan(userId, startDateTime, endDateTime, SORT_BY_DATE_TIME);
    }

    @Override
    public Meal getMealWithUser(int id, int userId) {
        Meal meal = get(id, userId);
        meal.setUser(crudUserRepository.findById(userId).orElseThrow());
        return meal;
    }
}
