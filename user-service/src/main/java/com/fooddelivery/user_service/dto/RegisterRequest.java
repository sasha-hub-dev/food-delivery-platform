package com.fooddelivery.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Введите корректный формат email")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 40, message = "Пароль должен содержать от 6 до 40 символов")
    private String password;

    @NotBlank(message = "Полное имя не может быть пустым")
    private String fullName;
}