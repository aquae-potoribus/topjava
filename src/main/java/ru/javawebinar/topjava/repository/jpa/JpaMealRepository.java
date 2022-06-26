package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            em.createNativeQuery("INSERT INTO meals (user_id, date_time, description, calories) VALUES (?,?,?,?)")
                    .setParameter(1, userId)
                    .setParameter(2, meal.getDateTime())
                    .setParameter(3, meal.getDescription())
                    .setParameter(4, meal.getCalories())
                    .executeUpdate();
            return meal;
        } else {
            if(userId != Integer.parseInt(em.createNativeQuery("SELECT user_id from meals WHERE id=:meal_id")
                    .setParameter("meal_id", meal.getId()).getSingleResult().toString())
                    ) {
                throw new NotFoundException("");
            }
            em.createNativeQuery("UPDATE meals SET date_time=?, description=?, calories=?  WHERE id=?")
                    .setParameter(1, meal.getDateTime())
                    .setParameter(2, meal.getDescription())
                    .setParameter(3, meal.getCalories())
                    .setParameter(4, meal.getId())
                    .executeUpdate();
            return meal;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return  em.find(Meal.class, id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("user_id", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.BETWEEN_HALF_OPEN, Meal.class)
                .setParameter("user_id", userId)
                .setParameter("start_date_time", startDateTime)
                .setParameter("end_date_time", endDateTime)
                .getResultList();
    }
}