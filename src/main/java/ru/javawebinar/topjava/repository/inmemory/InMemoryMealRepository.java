package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap(Stream.of(new Object[][] {
            { 42, new Meal(1 ,LocalDateTime.now(), "завтрак", 500) },
            { 43, new Meal(1 ,LocalDateTime.now(), "обед", 700) },
            { 44, new Meal(2 ,LocalDateTime.now(), "ужин", 500) },
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Meal) data[1])));
    private final AtomicInteger counter = new AtomicInteger(0);
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);



    @Override
    public Meal save(Meal meal, Integer userId) {
        if(meal == null){
            throw new NotFoundException("meal не должен быть null");
        }
        if (meal.isNew()) {
            meal.setUserId(userId);
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }

        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> {
            if (!Objects.equals(oldMeal.getUserId(), userId)) {
                log.error("нельзя обновить еду другого пользователя");
                return null;
            }
            meal.setUserId(userId);
            return meal;
        });
    }

    @Override
    public boolean delete(int id, Integer userId) {
        repository.computeIfPresent(id, (mealId, meal) -> {
            if (!Objects.equals(meal.getUserId(), userId)) {
                return meal;
            }
            repository.remove(id);
            return null;
        });
        return !repository.containsKey(id);
    }

    @Override
    public Meal get(int id, Integer userId) {
        Meal meal = repository.get(id);
        if (meal == null || !Objects.equals(meal.getUserId(), userId)) {
            log.info("нельзя получить еду другого пользователя/null");
            return null;
        }
        return meal;
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        return repository
                .values()
                .stream()
                .filter(meal -> Objects.equals(meal.getUserId(), userId))
                .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}

