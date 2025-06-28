package com.pixel.mealz.mealz.features.meal.repository;

import com.pixel.mealz.mealz.features.auth.model.User;
import com.pixel.mealz.mealz.features.meal.model.MealHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MealHistoryRepository extends JpaRepository<MealHistory, UUID> {
    List<MealHistory> findByUser(User user);

    Optional<MealHistory> findByUserAndDate(User user, LocalDate date);
}