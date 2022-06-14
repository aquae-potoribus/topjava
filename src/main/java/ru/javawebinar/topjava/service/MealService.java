package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

@Service
public class MealService {
    private final MealRepository mealRepository;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public Meal create(Meal meal, int userId) {
        return ValidationUtil.checkNotFoundWithId(mealRepository.save(meal, userId), userId);
    }

    public void delete(int id, int userId) {
        ValidationUtil.checkNotFoundWithId(mealRepository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        return ValidationUtil.checkNotFoundWithId(mealRepository.get(id, userId), id);

    }

    public List<Meal> getAll(int userId) {
        return new ArrayList<>(mealRepository.getAll(userId));
    }

    public void update(Meal meal, int userId) {
        mealRepository.save(meal, userId);

    }

}