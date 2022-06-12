package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    @Override
    public Meal save(Meal meal, Integer userId) {
        if (meal.isNew()) {
            meal.setUserId(SecurityUtil.authUserId());
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
            meal.setUserId(SecurityUtil.authUserId());
            return meal;
        });
    }

    @Override
    public boolean delete(int id, Integer userId) {
        repository.computeIfPresent(id, (k, v) -> {
            if (!Objects.equals(v.getUserId(), userId)) {
                throw new IllegalArgumentException("нельзя удалить еду другого пользователя");
            }
            return repository.remove(id);
        });
        return true;
    }

    @Override
    public Meal get(int id, Integer userId) {
        Meal meal = repository.get(id);
        if (meal == null) {
            log.error("такого meal id нет");
            return null;
        }
        if (!Objects.equals(meal.getUserId(), userId)) {
            log.error("нельзя получить еду другого пользователя");
            return null;
        }
        return meal;
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return repository
                .values()
                .stream()
                .filter(x -> Objects.equals(x.getUserId(), userId))
                .sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()))
                .collect(Collectors.toList());
    }
}

