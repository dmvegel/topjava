package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    private final MealService mealService;

    public JspMealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping("/meals/create")
    protected String createMeal(@ModelAttribute("meal") MealTo mealTo) {
        int userId = SecurityUtil.authUserId();
        log.info("create meal for user {}", userId);
        mealService.create(new Meal(mealTo.getDateTime(), mealTo.getDescription(), mealTo.getCalories()), userId);
        return "redirect:/meals";
    }

    @PostMapping("/meals/delete")
    protected String deleteMeal(@RequestParam("id") int id,
                                @RequestHeader(value = "Referer", required = false) String referer) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user {}", id, userId);
        mealService.delete(id, userId);
        return referer != null ? "redirect:" + referer : "redirect:/meals";
    }

    @PostMapping("/meals/update")
    protected String updateMeal(@ModelAttribute MealTo mealTo) {
        int userId = SecurityUtil.authUserId();
        log.info("update meal {} for user {}", mealTo.getId(), userId);
        Meal meal = mealService.get(mealTo.getId(), userId);
        assureIdConsistent(meal, mealTo.getId());
        meal.setDateTime(mealTo.getDateTime());
        meal.setCalories(mealTo.getCalories());
        meal.setDescription(mealTo.getDescription());
        mealService.update(meal, userId);
        return "redirect:/meals";
    }

    @GetMapping("/meals/update")
    protected String updateMealForm(@ModelAttribute MealTo mealTo) {
        log.info("GET update meal form for meal {} user {}", mealTo.getId(), SecurityUtil.authUserId());
        return "mealForm";
    }

    @GetMapping("/meals/create")
    protected String createMealForm(Model model) {
        log.info("GET create meal form for user {}", SecurityUtil.authUserId());
        model.addAttribute("mealTo", MealTo.empty());
        return "mealForm";
    }

    @GetMapping("/meals")
    protected String doGetAll(Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        model.addAttribute("meals", MealsUtil.getTos(mealService.getAll(userId),
                SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping(value = "/meals/filter")
    protected String doFilter(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                              @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                              @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                              Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        model.addAttribute("meals", MealsUtil.getFilteredTos(
                mealService.getBetweenInclusive(startDate, endDate, userId),
                SecurityUtil.authUserCaloriesPerDay(),
                startTime,
                endTime));
        return "meals";
    }
}
