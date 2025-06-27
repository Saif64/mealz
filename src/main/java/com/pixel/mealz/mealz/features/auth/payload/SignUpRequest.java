package com.pixel.mealz.mealz.features.auth.payload;

import com.pixel.mealz.mealz.features.auth.model.LocationName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotNull
    private LocationName locationName;


    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String employeeName;
}
