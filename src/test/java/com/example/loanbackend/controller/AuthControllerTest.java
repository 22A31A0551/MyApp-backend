package com.example.loanbackend.controller;

import com.example.loanbackend.dto.AuthResponse;
import com.example.loanbackend.model.User;
import com.example.loanbackend.repository.UserRepository;
import com.example.loanbackend.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setPhone("9876543210");
        sampleUser.setPassword("plainPassword123");
    }

    @Test
    void testRegisterNewUserSuccess() {
        when(userRepository.existsByPhone("9876543210")).thenReturn(false);
        when(passwordEncoder.encode("plainPassword123")).thenReturn("$2a$10$hashedPassword");

        String result = authController.register(sampleUser);

        assertEquals("Registered successfully ✅", result);
        verify(passwordEncoder).encode("plainPassword123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterExistingUserFails() {
        when(userRepository.existsByPhone("9876543210")).thenReturn(true);

        String result = authController.register(sampleUser);

        assertEquals("User already exists ❌", result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginWithBcryptHashedPassword() {
        User existingUser = new User();
        existingUser.setPhone("9876543210");
        existingUser.setPassword("$2a$10$alreadyHashedPassword");

        when(userRepository.findByPhone("9876543210")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("plainPassword123", "$2a$10$alreadyHashedPassword")).thenReturn(true);
        when(jwtUtils.generateToken("9876543210")).thenReturn("mock.jwt.token");

        ResponseEntity<?> response = authController.login(sampleUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof AuthResponse);

        AuthResponse authResponse = (AuthResponse) response.getBody();
        assertEquals("mock.jwt.token", authResponse.getToken());
        assertEquals("9876543210", authResponse.getPhone());
    }

    @Test
    void testLoginWithLegacyPlaintextPasswordAutoUpgradesToBcrypt() {
        // Legacy user with plain text password in DB
        User legacyUser = new User();
        legacyUser.setPhone("9876543210");
        legacyUser.setPassword("plainPassword123");

        when(userRepository.findByPhone("9876543210")).thenReturn(Optional.of(legacyUser));
        when(passwordEncoder.encode("plainPassword123")).thenReturn("$2a$10$newlyHashedPassword");
        when(jwtUtils.generateToken("9876543210")).thenReturn("mock.jwt.token");

        ResponseEntity<?> response = authController.login(sampleUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponse authResponse = (AuthResponse) response.getBody();
        assertNotNull(authResponse);
        assertEquals("mock.jwt.token", authResponse.getToken());

        // Verify that the legacy user was upgraded to BCrypt and saved to DB
        assertEquals("$2a$10$newlyHashedPassword", legacyUser.getPassword());
        verify(userRepository).save(legacyUser);
    }

    @Test
    void testLoginWithWrongPasswordFails() {
        User existingUser = new User();
        existingUser.setPhone("9876543210");
        existingUser.setPassword("$2a$10$hashedPassword");

        when(userRepository.findByPhone("9876543210")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        ResponseEntity<?> response = authController.login(sampleUser);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testLoginWithNonExistentPhoneFails() {
        when(userRepository.findByPhone("9876543210")).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.login(sampleUser);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }
}
