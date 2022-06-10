package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private static final Logger log = LoggerFactory.getLogger(MealService.class);
    private final MealRepository mealRepository;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public Meal create(Meal meal, int userId) {
        meal.setUserId(userId);
        return mealRepository.save(meal);
    }

    public void delete(int id, int userId) {
        if(userId != mealRepository.get(id).getUserId()) {
            throw new NotFoundException("нельзя удалить еду другого пользователя");
        }
        checkNotFoundWithId(mealRepository.delete(id), id);
    }

    public Meal get(int id,int userId) {
        if(userId != mealRepository.get(id).getUserId()) {
            throw new NotFoundException("нельзя получить еду другого пользователя");
        }
        return checkNotFoundWithId(mealRepository.get(id), id);
    }

    public List<Meal> getAll(int userId) {
        return mealRepository.getAll()
                .stream()
                .filter(x -> x.getUserId() == SecurityUtil.authUserId())
                .collect(Collectors.toList());
    }

    public void update(Meal meal,int userId) {
        if(userId == mealRepository.get(meal.getId()).getUserId()) {
            checkNotFoundWithId(mealRepository.save(meal), meal.getId());
        } else {
            throw new NotFoundException("нельзя обновить еду другого пользователя");
        }
    }

}