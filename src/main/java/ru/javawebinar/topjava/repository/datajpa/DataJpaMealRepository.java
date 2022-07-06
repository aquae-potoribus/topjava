package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_DATE = Sort.by(Sort.Direction.ASC, "date_time");

    private final CrudMealRepository crudRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew()) {
            if (crudRepository.findById(meal.getId()).get().getId() != userId) {
                return null;
            }
        }
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (crudRepository.findById(id).get().getId() != userId) {
            return false;
        }
        crudRepository.deleteById(id);
        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        if (crudRepository.findById(id).get().getId() != userId) {
            return null;
        }
        return crudRepository.findById(id).get();
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAll(SORT_DATE);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getBetweenHalfOpen(startDateTime,endDateTime,userId);
    }
}
