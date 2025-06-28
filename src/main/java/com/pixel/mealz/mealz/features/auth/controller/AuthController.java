package com.pixel.mealz.mealz.features.auth.controller;

import com.pixel.mealz.mealz.features.auth.model.LocationName;
import com.pixel.mealz.mealz.features.auth.model.Role;
import com.pixel.mealz.mealz.features.auth.model.User;
import com.pixel.mealz.mealz.features.auth.payload.*;
import com.pixel.mealz.mealz.features.auth.repository.UserRepository;
import com.pixel.mealz.mealz.features.auth.security.JwtTokenProvider;
import com.pixel.mealz.mealz.features.meal.model.MealChoice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

        if (tokenProvider.validateToken(requestRefreshToken)) {
            String username = tokenProvider.getUsernameFromJWT(requestRefreshToken);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

            String newAccessToken = tokenProvider.generateAccessToken(username, user.getRole());
            return ResponseEntity.ok(new JwtAuthenticationResponse(newAccessToken, requestRefreshToken));
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Invalid refresh token!"), HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setLocationName(signUpRequest.getLocationName());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setEmployeeName(signUpRequest.getEmployeeName());
        user.setDefaultSundayTuesdayWednesday(MealChoice.MEAL_OFF);
        user.setDefaultMonday(MealChoice.MEAL_OFF);
        user.setDefaultThursday(MealChoice.MEAL_OFF);


        if (signUpRequest.getLocationName() == LocationName.SKS) {
            user.setRole(Role.ROLE_SKS);
        } else if (signUpRequest.getLocationName() == LocationName.SHADHINOTA) {
            user.setRole(Role.ROLE_SHADHINOTA);
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Invalid location specified!"),
                    HttpStatus.BAD_REQUEST);
        }

        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }
}