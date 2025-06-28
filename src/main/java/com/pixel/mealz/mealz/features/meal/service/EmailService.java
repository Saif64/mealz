package com.pixel.mealz.mealz.features.meal.service;

import com.pixel.mealz.mealz.features.meal.model.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMealEmail(List<Meal> meals) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("saifmahmud81@gmail.com");
        message.setTo("muntasir.mahmud@vivasoftltd.com");
        message.setSubject("Meal Choices for Tomorrow");

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the meal choices for tomorrow:\n\n");
        for (Meal meal : meals) {
            sb.append(meal.getUser().getEmployeeName())
                    .append(": ")
                    .append(meal.getMealChoice())
                    .append("\n");
        }
        message.setText(sb.toString());

        mailSender.send(message);
    }
}