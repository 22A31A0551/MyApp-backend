package com.example.loanbackend.controller;

import com.example.loanbackend.dto.AuthResponse;
import com.example.loanbackend.model.User;
import com.example.loanbackend.repository.UserRepository;
import com.example.loanbackend.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (user.getPhone() == null || user.getPhone().trim().isEmpty() ||
            user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return "Phone and password are required ❌";
        }

        if (userRepository.existsByPhone(user.getPhone().trim())) {
            return "User already exists ❌";
        }

        // Encrypt password using BCrypt before storing
        user.setPhone(user.getPhone().trim());
        user.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        userRepository.save(user);

        return "Registered successfully ✅";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        if (loginUser.getPhone() == null || loginUser.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String phone = loginUser.getPhone().trim();
        String rawPassword = loginUser.getPassword().trim();

        Optional<User> optionalUser = userRepository.findByPhone(phone);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User user = optionalUser.get();
        String storedPassword = user.getPassword();
        boolean matches = false;

        // Check if stored password is BCrypt hashed
        if (storedPassword != null && (storedPassword.startsWith("$2a$") ||
                storedPassword.startsWith("$2b$") ||
                storedPassword.startsWith("$2y$"))) {
            matches = passwordEncoder.matches(rawPassword, storedPassword);
        } else {
            // Legacy plaintext password check for existing production users
            if (storedPassword != null && storedPassword.equals(rawPassword)) {
                matches = true;
                // Seamlessly upgrade plaintext password to BCrypt in database
                user.setPassword(passwordEncoder.encode(rawPassword));
                userRepository.save(user);
            }
        }

        if (!matches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Generate JWT token
        String token = jwtUtils.generateToken(user.getPhone());

        // Return user info and token (without password)
        AuthResponse response = new AuthResponse(token, user.getId(), user.getPhone(), "Login successful");
        return ResponseEntity.ok(response);
    }
}