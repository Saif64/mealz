package com.pixel.mealz.mealz.features.user.controller;

import com.pixel.mealz.mealz.features.auth.model.User;
import com.pixel.mealz.mealz.features.auth.payload.ApiResponse;
import com.pixel.mealz.mealz.features.user.payload.UserUpdateRequest;
import com.pixel.mealz.mealz.features.user.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/preferences")
public class UserPreferenceController {

    @Autowired
    private UserPreferenceService userPreferenceService;

    @GetMapping
    public ResponseEntity<User> getUserPreferences(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userPreferenceService.getUserPreferences(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse> partialUpdateUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserUpdateRequest updateRequest) {
        userPreferenceService.partialUpdateUser(userDetails.getUsername(), updateRequest);
        return ResponseEntity.ok(new ApiResponse(true, "User information has been updated."));
    }
}