package main.java.ru.javawebinar.topjava.util;

import main.java.ru.javawebinar.topjava.model.UserMeal;
import main.java.ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(14, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesEveryDay = new HashMap<>();
        for (UserMeal userMeal : meals) {
            LocalDate userLocalDate = userMeal.getDateTime().toLocalDate();
            caloriesEveryDay.merge(userLocalDate, userMeal.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> listMeals = new ArrayList<>();
        AtomicInteger caloriesDay = new AtomicInteger();

        for (UserMeal userMeal : meals) {
            caloriesDay.set(caloriesEveryDay.get(userMeal.getDateTime().toLocalDate()));
            LocalTime userTime = userMeal.getDateTime().toLocalTime();

            if (userTime.compareTo(startTime) >= 0 && userTime.compareTo(endTime) <= 0) {
                listMeals.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(),
                        userMeal.getCalories(), caloriesDay.get() > caloriesPerDay));
            }
        }
        return listMeals;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesEveryDay = new HashMap<>();
        meals.stream()
                .peek(userMeal -> caloriesEveryDay.merge(userMeal.getDateTime().toLocalDate(),
                        userMeal.getCalories(), Integer::sum))
                .collect(Collectors.toList());
        AtomicInteger caloriesDay = new AtomicInteger();
        return meals.stream()
                .map(userMeal -> {

                    caloriesDay.set(caloriesEveryDay.get(userMeal.getDateTime().toLocalDate()));
                    LocalTime userTime = userMeal.getDateTime().toLocalTime();

                    if (userTime.compareTo(startTime) >= 0 && userTime.compareTo(endTime) <= 0) {
                        return new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                                caloriesDay.get() > caloriesPerDay);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}