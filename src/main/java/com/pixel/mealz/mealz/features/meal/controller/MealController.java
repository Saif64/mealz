package com.pixel.mealz.mealz.features.meal.controller;

import com.pixel.mealz.mealz.features.auth.model.User;
import com.pixel.mealz.mealz.features.auth.payload.ApiResponse;
import com.pixel.mealz.mealz.features.auth.repository.UserRepository;
import com.pixel.mealz.mealz.features.meal.model.Meal;
import com.pixel.mealz.mealz.features.meal.model.MealHistory;
import com.pixel.mealz.mealz.features.meal.payload.MealChoiceRequest;
import com.pixel.mealz.mealz.features.meal.service.MealService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/meal")
public class MealController {

    @Autowired
    private MealService mealService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/choice")
    public ResponseEntity<ApiResponse> setMealChoice(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody MealChoiceRequest mealChoiceRequest) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Authenticated user not found in the database."));
            mealService.setMealChoice(user, mealChoiceRequest.getMealChoice(), mealChoiceRequest.getDate());
            return ResponseEntity.ok(new ApiResponse(true, "Meal choice has been successfully updated for " + mealChoiceRequest.getDate()));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(false, "An internal error occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getMealData(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) UUID mealId) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Authenticated user not found in the database."));

            if (mealId != null) {
                // Fetch a single meal by ID
                Optional<Meal> mealOptional = mealService.getMealById(mealId);
                if (mealOptional.isPresent()) {
                    // Ensure the fetched meal belongs to the authenticated user
                    if (mealOptional.get().getUser().getId().equals(user.getId())) {
                        return ResponseEntity.ok(mealOptional.get());
                    } else {
                        return new ResponseEntity<>(new ApiResponse(false, "Access denied. You are not authorized to view this meal."), HttpStatus.FORBIDDEN);
                    }
                } else {
                    return new ResponseEntity<>(new ApiResponse(false, "Meal with ID " + mealId + " not found."), HttpStatus.NOT_FOUND);
                }
            } else {
                // Fetch the entire meal history for the user
                List<MealHistory> mealHistory = mealService.getMealHistory(user);
                return ResponseEntity.ok(mealHistory);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(false, "An internal error occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}