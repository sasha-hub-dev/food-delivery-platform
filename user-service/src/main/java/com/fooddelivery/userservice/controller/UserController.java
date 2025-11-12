package com.fooddelivery.userservice.controller;

import com.fooddelivery.userservice.dto.response.UserResponse;
import com.fooddelivery.userservice.dto.request.UserUpdateRequest;
import com.fooddelivery.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse response = userService.getCurrentUser(email);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(@RequestBody UserUpdateRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse response = userService.updateUser(email, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.deleteUser(email);
        return ResponseEntity.ok().build();
    }
}