package com.pixel.mealz.mealz.features.user.service;

import com.pixel.mealz.mealz.features.auth.model.User;
import com.pixel.mealz.mealz.features.auth.repository.UserRepository;
import com.pixel.mealz.mealz.features.user.payload.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPreferenceService {

    @Autowired
    private UserRepository userRepository;

    public User getUserPreferences(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void partialUpdateUser(String username, UserUpdateRequest updateRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update only the fields that are present in the request
        if (updateRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(updateRequest.getPhoneNumber());
        }
        if (updateRequest.getEmployeeName() != null) {
            user.setEmployeeName(updateRequest.getEmployeeName());
        }
        if (updateRequest.getDefaultSundayTuesdayWednesday() != null) {
            user.setDefaultSundayTuesdayWednesday(updateRequest.getDefaultSundayTuesdayWednesday());
        }
        if (updateRequest.getDefaultMonday() != null) {
            user.setDefaultMonday(updateRequest.getDefaultMonday());
        }
        if (updateRequest.getDefaultThursday() != null) {
            user.setDefaultThursday(updateRequest.getDefaultThursday());
        }

        userRepository.save(user);
    }
}