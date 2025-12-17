package com.fooddelivery.user_service.service;

import com.fooddelivery.user_service.dto.LoginRequest;
import com.fooddelivery.user_service.dto.RegisterRequest;
import com.fooddelivery.user_service.model.Role;
import com.fooddelivery.user_service.model.User;
import com.fooddelivery.user_service.repository.RoleRepository;
import com.fooddelivery.user_service.repository.UserRepository;
import com.fooddelivery.user_service.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email " + request.getEmail() + " уже занят!");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());

        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));


        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Ошибка: Роль USER не найдена."));
        user.getRoles().add(userRole);

        return userRepository.save(user);
    }

    public String authenticateUser(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.generateToken(authentication);
    }
}