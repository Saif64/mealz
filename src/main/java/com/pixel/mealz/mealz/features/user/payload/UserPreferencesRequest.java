package com.pixel.mealz.mealz.features.user.payload;

import com.pixel.mealz.mealz.features.meal.model.MealChoice;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPreferencesRequest {

    @NotNull
    private MealChoice defaultSundayTuesdayWednesday;

    @NotNull
    private MealChoice defaultMonday;

    @NotNull
    private MealChoice defaultThursday;
}