package com.pixel.mealz.mealz.features.meal.task;

import com.pixel.mealz.mealz.features.meal.model.Meal;
import com.pixel.mealz.mealz.features.meal.repository.MealRepository;
import com.pixel.mealz.mealz.features.meal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private EmailService emailService;

    // Runs every day at 3 PM
    @Scheduled(cron = "0 0 15 * * ?")
    public void sendMealEmail() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Meal> meals = mealRepository.findByDate(tomorrow);

        if (meals != null && !meals.isEmpty()) {
            emailService.sendMealEmail(meals);
        }
    }
}