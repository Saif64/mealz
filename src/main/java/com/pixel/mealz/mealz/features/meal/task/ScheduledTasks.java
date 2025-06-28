package com.pixel.mealz.mealz.features.meal.task;

import com.pixel.mealz.mealz.features.auth.model.User;
import com.pixel.mealz.mealz.features.auth.repository.UserRepository;
import com.pixel.mealz.mealz.features.meal.model.Meal;
import com.pixel.mealz.mealz.features.meal.model.MealChoice;
import com.pixel.mealz.mealz.features.meal.repository.MealRepository;
import com.pixel.mealz.mealz.features.meal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ScheduledTasks {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;


    @Scheduled(cron = "0 0 15 * * ?")
    public void sendMealEmail() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        Map<UUID, Meal> explicitChoices = mealRepository.findByDate(tomorrow)
                .stream()
                .collect(Collectors.toMap(meal -> meal.getUser().getId(), meal -> meal));


        List<User> allUsers = userRepository.findAll();

        List<Meal> finalMealList = new ArrayList<>();

        for (User user : allUsers) {
            Meal finalMeal = new Meal();
            finalMeal.setUser(user);
            finalMeal.setDate(tomorrow);

            if (explicitChoices.containsKey(user.getId())) {
                finalMeal.setMealChoice(explicitChoices.get(user.getId()).getMealChoice());
            } else {

                DayOfWeek day = tomorrow.getDayOfWeek();
                switch (day) {
                    case MONDAY:
                        finalMeal.setMealChoice(user.getDefaultMonday());
                        break;
                    case THURSDAY:
                        finalMeal.setMealChoice(user.getDefaultThursday());
                        break;
                    case SUNDAY:
                    case TUESDAY:
                    case WEDNESDAY:
                        finalMeal.setMealChoice(user.getDefaultSundayTuesdayWednesday());
                        break;
                    default:
                        
                        finalMeal.setMealChoice(MealChoice.MEAL_OFF);
                        break;
                }
            }
            finalMealList.add(finalMeal);
        }

        if (!finalMealList.isEmpty()) {
            emailService.sendMealEmail(finalMealList);
        }
    }
}