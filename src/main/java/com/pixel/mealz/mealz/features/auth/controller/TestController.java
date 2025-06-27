package com.pixel.mealz.mealz.features.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('SKS') or hasRole('SHADHINOTA')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/sks")
    @PreAuthorize("hasRole('SKS')")
    public String sksAccess() {
        return "SKS Board.";
    }

    @GetMapping("/shadhinota")
    @PreAuthorize("hasRole('SHADHINOTA')")
    public String shadhinotaAccess() {
        return "Shadhinota Board.";
    }
}
