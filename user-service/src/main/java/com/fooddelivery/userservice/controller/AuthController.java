package com.fooddelivery.userservice.controller;

import com.fooddelivery.userservice.dto.request.LoginRequest;
import com.fooddelivery.userservice.dto.request.RegisterRequest;
import com.fooddelivery.userservice.dto.response.LoginResponse;
import com.fooddelivery.userservice.dto.response.RegisterResponse;
import com.fooddelivery.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}