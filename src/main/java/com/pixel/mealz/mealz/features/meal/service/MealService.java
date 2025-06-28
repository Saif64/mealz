package com.pixel.mealz.mealz.features.meal.service;

import com.pixel.mealz.mealz.features.auth.model.User;
import com.pixel.mealz.mealz.features.meal.model.Meal;
import com.pixel.mealz.mealz.features.meal.model.MealChoice;
import com.pixel.mealz.mealz.features.meal.model.MealHistory;
import com.pixel.mealz.mealz.features.meal.repository.MealHistoryRepository;
import com.pixel.mealz.mealz.features.meal.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MealService {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private MealHistoryRepository mealHistoryRepository;

    public void setMealChoice(User user, MealChoice mealChoice, LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot change meal choice for a past date.");
        }
        
        Optional<Meal> existingMeal = mealRepository.findByUserAndDate(user, date);
        Meal meal;
        if (existingMeal.isPresent()) {
            meal = existingMeal.get();
            meal.setMealChoice(mealChoice);
        } else {
            meal = new Meal();
            meal.setUser(user);
            meal.setDate(date);
            meal.setMealChoice(mealChoice);
        }
        mealRepository.save(meal);


        Optional<MealHistory> existingHistory = mealHistoryRepository.findByUserAndDate(user, date);
        MealHistory mealHistory;
        if (existingHistory.isPresent()) {
            mealHistory = existingHistory.get();
            mealHistory.setMealChoice(mealChoice);
        } else {
            mealHistory = new MealHistory();
            mealHistory.setUser(user);
            mealHistory.setDate(date);
            mealHistory.setMealChoice(mealChoice);
        }
        mealHistoryRepository.save(mealHistory);
    }

    public Optional<Meal> getMealById(UUID mealId) {
        return mealRepository.findById(mealId);
    }

    public List<MealHistory> getMealHistory(User user) {
        return mealHistoryRepository.findByUser(user);
    }
}