package com.fooddelivery.user_service.controller;

import com.fooddelivery.user_service.dto.JwtAuthenticationResponse;
import com.fooddelivery.user_service.dto.LoginRequest;
import com.fooddelivery.user_service.dto.RegisterRequest;
import com.fooddelivery.user_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация", description = "Регистрация и вход пользователя")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        authService.registerUser(request);
        return new ResponseEntity<>("Пользователь успешно зарегистрирован!", HttpStatus.CREATED);
    }

        @Operation(summary = "Вход пользователя и получение JWT-токена")
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
        String jwt = authService.authenticateUser(request);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
}