package main.java.ru.javawebinar.topjava.util;

import main.java.ru.javawebinar.topjava.model.UserMeal;
import main.java.ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;


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

        List<UserMealWithExcess> listMeals = new ArrayList<>();

        Map<LocalDate, Integer> caloriesEveryDay = new HashMap<>();
        for (UserMeal userMeal : meals) {
            LocalDate userLocalDate = userMeal.getDateTime().toLocalDate();
            if (!caloriesEveryDay.containsKey(userLocalDate)) {
                caloriesEveryDay.put(userLocalDate, userMeal.getCalories());
            } else {
                caloriesEveryDay.replace(userLocalDate, caloriesEveryDay.get(userLocalDate) + userMeal.getCalories());
            }
        }

        for (UserMeal userMeal : meals) {
            userMeal.setCaloriesDay(caloriesEveryDay.get(userMeal.getDateTime().toLocalDate()));
            if ((userMeal.getDateTime().getHour() < endTime.getHour() || userMeal.getDateTime().toLocalTime().equals(startTime)) &&
                    (userMeal.getDateTime().getHour() > startTime.getHour() || (userMeal.getDateTime().getHour() == startTime.getHour() && userMeal.getDateTime().getMinute() >= startTime.getMinute()))) {
                listMeals.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), userMeal.getCaloriesDay() > caloriesPerDay));
            }
        }

        return listMeals;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<LocalDate> s = meals.stream().filter(distinctByKey(Meal -> Meal.getDateTime().toLocalDate())).map(userMeal -> userMeal.getDateTime().toLocalDate()).toList();

        for (int i = 0; i < s.size(); i++) {
            int finalI = i;
            int caloriesDay = meals.stream().filter(a -> a.getDateTime().toLocalDate().equals(s.get(finalI))).mapToInt(UserMeal::getCalories).sum();
            List<UserMeal> empty = meals.stream().filter(a -> a.getDateTime().toLocalDate().equals(s.get(finalI))).peek(k -> k.setCaloriesDay(caloriesDay)).toList();
        }

        return meals.stream().filter(userMeal ->
                ((userMeal.getDateTime().getHour() < endTime.getHour() || userMeal.getDateTime().toLocalTime().equals(startTime)) &&
                        (userMeal.getDateTime().getHour() > startTime.getHour() || (userMeal.getDateTime().getHour() == startTime.getHour() && userMeal.getDateTime().getMinute() >= startTime.getMinute())))
        ).map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), userMeal.getCaloriesDay() > caloriesPerDay)).toList();
    }
}