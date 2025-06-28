package com.pixel.mealz.mealz.features.meal.payload;

import com.pixel.mealz.mealz.features.meal.model.MealChoice;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MealChoiceRequest {

    @NotNull
    private MealChoice mealChoice;

    @NotNull
    private LocalDate date;
}