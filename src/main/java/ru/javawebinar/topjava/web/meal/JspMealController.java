package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {
    public JspMealController(MealService service) {
        super(service);
    }

    @PostMapping("/create")
    public String doCreate(@ModelAttribute("meal") Meal meal) {
        create(meal);
        return "redirect:/meals";
    }

    @PostMapping("/delete")
    public String doDelete(@RequestParam("id") int id,
                           @RequestHeader(value = "Referer", required = false) String referer) {
        delete(id);
        return referer != null ? "redirect:" + referer : "redirect:/meals";
    }

    @PostMapping("/update")
    public String doUpdate(@ModelAttribute Meal meal) {
        update(meal, meal.getId());
        return "redirect:/meals";
    }

    @GetMapping("/update")
    public String updateForm(@RequestParam("id") int id, Model model) {
        Meal meal = get(id);
        model.addAttribute("meal",
                new Meal(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories()));
        return "mealForm";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("meal", new Meal());
        return "mealForm";
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", getAll());
        return "meals";
    }

    @GetMapping(value = "/filter")
    public String filter(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                         @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                         @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                         Model model) {
        model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}
