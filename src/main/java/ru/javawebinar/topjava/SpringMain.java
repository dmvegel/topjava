package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.create(new Meal(null,
                    LocalDateTime.of(2020, 1, 1, 10, 1), "description1", 1000));

            System.out.println(mealRestController.get(1));
            System.out.println(mealRestController.getAll());
            mealRestController.delete(2);
            System.out.println(mealRestController.getAll());
            mealRestController.update(new Meal(3,
                    LocalDateTime.of(2020, 1, 1, 10, 4), "description2", 1000), 3);
            System.out.println(mealRestController.getAll());
            System.out.println(mealRestController.getInRange(
                    LocalDate.of(2020, 1, 30),
                    LocalDate.of(2020, 2, 1),
                    null,
                    null)
            );
            System.out.println(mealRestController.getInRange(
                    LocalDate.of(2020, 1, 31),
                    LocalDate.of(2020, 1, 31),
                    LocalTime.of(0, 0, 0),
                    LocalTime.of(0, 0, 0))
            );
            SecurityUtil.setAuthUserId(2);
            System.out.println(mealRestController.getAll());
//            System.out.println(mealRestController.get(100)); // not found
//            mealRestController.update(new Meal(3,
//                    LocalDateTime.of(2020, 1, 1, 10, 4), "description2", 1000), 2); // IAE
        }
    }
}
