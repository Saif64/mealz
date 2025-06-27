package com.pixel.mealz.mealz.features.auth.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRefreshRequest {

    @NotBlank
    private String refreshToken;
}
