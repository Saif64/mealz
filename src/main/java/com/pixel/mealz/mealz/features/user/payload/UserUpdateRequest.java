package com.pixel.mealz.mealz.features.user.payload;


import com.pixel.mealz.mealz.features.meal.model.MealChoice;
import lombok.Data;

@Data
public class UserUpdateRequest {

    private String phoneNumber;

    private String employeeName;

    private MealChoice defaultSundayTuesdayWednesday;

    private MealChoice defaultMonday;

    private MealChoice defaultThursday;
}
