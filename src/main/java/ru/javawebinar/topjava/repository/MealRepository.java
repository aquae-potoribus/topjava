package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {
    // null if updated meal does not belong to userId

    Meal save(Meal meal, Integer userId);

    // false if meal does not belong to userId
    boolean delete(int id, Integer userId);

    // null if meal does not belong to userId
    Meal get(int id, Integer userId);

    // ORDERED dateTime desc
    List<Meal> getAll(Integer userId);
}
