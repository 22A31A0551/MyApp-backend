package com.example.loanbackend.controller;

import com.example.loanbackend.model.User;
import com.example.loanbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        if (userRepository.existsByPhone(user.getPhone())) {
            return "User already exists ❌";
        }

        userRepository.save(user);
        return "Registered successfully ✅";
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {

        return userRepository
                .findByPhoneAndPassword(user.getPhone(), user.getPassword())

                .orElse(null);
    }
}