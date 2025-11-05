package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.List;

import static ru.javawebinar.topjava.repository.datajpa.DataJpaMealRepository.SORT_BY_DATE_TIME;

@Repository
public class DataJpaUserRepository implements UserRepository {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final CrudUserRepository crudUserRepository;
    private final CrudMealRepository crudMealRepository;

    public DataJpaUserRepository(CrudUserRepository crudRepository, CrudMealRepository crudMealRepository) {
        this.crudUserRepository = crudRepository;
        this.crudMealRepository = crudMealRepository;
    }

    @Override
    @Transactional
    public User save(User user) {
        return crudUserRepository.save(user);
    }

    @Override
    public boolean delete(int id) {
        return crudUserRepository.delete(id) != 0;
    }

    @Override
    public User get(int id) {
        return crudUserRepository.findById(id).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return crudUserRepository.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return crudUserRepository.findAll(SORT_NAME_EMAIL);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserWithMeals(int userId) {
        User user = get(userId);
        user.setMeals(crudMealRepository.getByUserId(userId, SORT_BY_DATE_TIME));
        return user;
    }
}
