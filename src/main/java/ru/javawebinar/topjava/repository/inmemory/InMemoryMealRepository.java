package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public synchronized Meal save(Meal meal) {

        if (meal.isNew()) {
            meal.setUserId(SecurityUtil.authUserId());
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }

        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> {
            if (repository.get(meal.getId()).getUserId() != SecurityUtil.authUserId()) {
                log.error("нельзя обновить еду другого пользователя");
                return null;
            }
            meal.setUserId(SecurityUtil.authUserId());
            return meal;
        });
    }

    @Override
    public synchronized boolean delete(int id) {
        if (repository.get(id).getUserId() != SecurityUtil.authUserId()) {
            log.error("нельзя удалить еду другого пользователя");
            return false;
        }
        return repository.remove(id) != null;
    }

    @Override
    public synchronized Meal get(int id) {
        if (repository.get(id).getUserId() != SecurityUtil.authUserId()) {
            log.error("нельзя получить еду другого пользователя");
            return null;
        }
        return repository.get(id);
    }

    @Override
    public synchronized Collection<Meal> getAll() {
        return repository
                .values()
                .stream()
                .filter(x -> x.getUserId() == SecurityUtil.authUserId())
                .sorted((x,y) -> y.getDateTime().compareTo(x.getDateTime()))
                .collect(Collectors.toList());
    }
}

