package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealsDao;
import ru.javawebinar.topjava.storage.MealsMemoryDao;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealsDao dao;

    @Override
    public void init() {
        dao = new MealsMemoryDao();
        MealsUtil.getInitList().forEach(dao::create);
        log.debug("meals initialized with size {}", dao.getAll().size());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        if (action == null) {
            showMeals(request, response);
            return;
        }
        switch (action.toUpperCase()) {
            case MealsUtil.ACTION_DELETE:
                handleDelete(request, response);
                break;
            case MealsUtil.ACTION_EDIT:
                handleEdit(request, response);
                break;
            case MealsUtil.ACTION_CREATE:
                forwardToMeal(request, response, null);
                break;
            default:
                showMeals(request, response);
        }
    }

    private void showMeals(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mealsTo",
                MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY));
        log.debug("forward to meals.jsp");
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        dao.delete(id);
        log.debug("delete meal with id {}", id);
        log.debug("redirect to meals");
        response.sendRedirect("meals");
    }

    private void handleEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Meal meal = dao.get(id);
        forwardToMeal(request, response, meal);
    }

    private void forwardToMeal(HttpServletRequest request, HttpServletResponse response, Meal meal) throws ServletException, IOException {
        request.setAttribute("meal", meal);
        log.debug("forward to meal.jsp");
        request.getRequestDispatcher("meal.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = buildMeal(request);
        if (meal.getId() == null) {
            dao.create(meal);
            log.debug("create meal {}", meal);
        } else {
            dao.update(meal);
            log.debug("update meal {}", meal);
        }
        response.sendRedirect("meals");
    }

    private Meal buildMeal(HttpServletRequest request) {
        String idStr = request.getParameter("id");
        Integer id = (idStr == null || idStr.isEmpty()) ? null : Integer.parseInt(idStr);
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        return new Meal(id, dateTime, description, calories);
    }
}
