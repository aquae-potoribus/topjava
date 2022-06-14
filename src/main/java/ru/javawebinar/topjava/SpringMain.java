package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.time.LocalDateTime;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.create(new Meal(LocalDateTime.now(), "мой завтрак", 500));

            InMemoryMealRepository repository = appCtx.getBean(InMemoryMealRepository.class);
            System.out.printf("У юзера 1 %s записей\n", repository.getAll(1).size());
            System.out.printf("У юзера 2 %s записей\n", repository.getAll(2).size());
            Meal user2meal = repository.get(8, 2);
            System.out.println("У юзера 2 есть еда " + user2meal);
            System.out.println("Попытка обновить еду юзера 2 от имени юзера 1");
            Meal saved = repository.save(user2meal, 1);
            System.out.println("Вот что вернул save: " + saved);
            System.out.printf("У юзера 1 %s записей\n", repository.getAll(1).size());
            System.out.printf("У юзера 2 %s записей\n", repository.getAll(2).size());
        }
    }
}
