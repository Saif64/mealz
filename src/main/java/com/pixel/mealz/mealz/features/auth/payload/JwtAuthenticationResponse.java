package com.pixel.mealz.mealz.features.auth.payload;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtAuthenticationResponse {

    @NonNull
    private String accessToken;

    @NonNull
    private String refreshToken;

    private String tokenType = "Bearer";
}
