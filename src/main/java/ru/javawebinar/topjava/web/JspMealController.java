package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;

@Controller
public class JspMealController {

    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService service;

    @GetMapping("/t")
    public String getMeal(HttpServletRequest request) {
        log.info("getMeal");
        String action = request.getParameter("action");
        int userId = Integer.parseInt(request.getParameter("userId"));

        switch (action == null ? "all" : action) {
            case "delete" -> {
                return delete(request, getId(request), userId);
            }
            case "create", "update" -> {
                return createUpdate(request, action, userId, "", 1000);
            }
            case "filter" -> {
                return filter(request, userId);
            }
            default -> {
                return defaultAction(request,userId);
            }
        }
    }

    @PostMapping("/t")
    public String setMeal(HttpServletRequest request) {
        log.info("setMeal");
        int userId = Integer.parseInt(request.getParameter("userId"));
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        service.create(meal, userId);

        return "users";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private String delete(HttpServletRequest request, int id, int userId) {
        service.delete(id, userId);
        return "meals";
    }

    private String createUpdate(HttpServletRequest request, String action, int userId, String description, int calories) {
        final Meal meal = "create".equals(action) ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), description, calories) :
                service.get(getId(request), userId);
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    private String filter(HttpServletRequest request, int userId) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        request.setAttribute("meals", service.getBetweenInclusive(startDate, endDate, userId));
        return "meals";
    }

    private String defaultAction (HttpServletRequest request, int userId) {
        request.setAttribute("meals", service.getAll(userId));
        return "meals";
    }

    private String asd(HttpServletRequest request, int id, int userId) {
        service.delete(id, userId);
        return "meals";
    }

}
