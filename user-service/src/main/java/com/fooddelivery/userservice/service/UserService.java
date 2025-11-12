package com.fooddelivery.userservice.service;

import com.fooddelivery.userservice.dto.request.LoginRequest;
import com.fooddelivery.userservice.dto.request.RegisterRequest;
import com.fooddelivery.userservice.dto.request.UserUpdateRequest;
import com.fooddelivery.userservice.dto.response.LoginResponse;
import com.fooddelivery.userservice.dto.response.RegisterResponse;
import com.fooddelivery.userservice.dto.response.UserResponse;
import com.fooddelivery.userservice.entity.Role;
import com.fooddelivery.userservice.entity.User;
import com.fooddelivery.userservice.repository.AddressRepository;
import com.fooddelivery.userservice.entity.Address;
import com.fooddelivery.userservice.repository.RoleRepository;
import com.fooddelivery.userservice.repository.UserRepository;
import com.fooddelivery.userservice.security.JwtTokenProvider;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AddressRepository addressRepository;

    @PostConstruct
    public void initRoles() {
        System.out.println("=== INIT ROLES START ===");

        // Создаем роли если их нет
        if (roleRepository.findByName("USER").isEmpty()) {
            System.out.println("Creating USER role...");
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
            System.out.println("USER role created");
        } else {
            System.out.println("USER role already exists");
        }

        if (roleRepository.findByName("ADMIN").isEmpty()) {
            System.out.println("Creating ADMIN role...");
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
            System.out.println("ADMIN role created");
        } else {
            System.out.println("ADMIN role already exists");
        }

        System.out.println("=== INIT ROLES END ===");
    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        // Находим роль USER (должна быть создана при старте приложения)
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        // 1. СОЗДАЕМ ПОЛЬЗОВАТЕЛЯ
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.getRoles().add(userRole); // Добавляем роль

        // 2. СОЗДАЕМ АДРЕС (если указан)
        if (request.getStreet() != null && !request.getStreet().trim().isEmpty()) {
            Address address = new Address();
            address.setStreet(request.getStreet());
            address.setCity(request.getCity());
            address.setZipCode(request.getZipCode());
            address.setCountry(request.getCountry());
            address.setUser(user); // Связываем с пользователем
            user.getAddresses().add(address); // Добавляем в коллекцию пользователя
        }

        // 3. СОХРАНЯЕМ ВСЕ ОДНИМ ЗАПРОСОМ (каскадное сохранение)
        User savedUser = userRepository.save(user);

        // 4. ГЕНЕРИРУЕМ ТОКЕНЫ
        String accessToken = jwtTokenProvider.generateAccessToken(savedUser.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.getEmail());

        // 5. СОЗДАЕМ ОТВЕТ
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setFullName(savedUser.getFullName());

        RegisterResponse response = new RegisterResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUser(userResponse);

        return response;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setFullName(user.getFullName());

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUser(userResponse);

        return response;
    }

    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());

        return response;
    }

    public UserResponse updateUser(String email, UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(request.getFullName());

        // Обновляем адрес если есть
        if (!user.getAddresses().isEmpty()) {
            Address address = user.getAddresses().get(0);
            address.setStreet(request.getStreet());
            address.setCity(request.getCity());
            address.setZipCode(request.getZipCode());
            address.setCountry(request.getCountry());
        }

        User updatedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(updatedUser.getId());
        response.setEmail(updatedUser.getEmail());
        response.setFullName(updatedUser.getFullName());

        return response;
    }

    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}