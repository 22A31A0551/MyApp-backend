package com.example.loanbackend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        // Set a test 256-bit secret key (64 hex characters) and expiration (1 hour)
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000L);
    }

    @Test
    void testGenerateAndValidateToken() {
        String phone = "9876543210";
        String token = jwtUtils.generateToken(phone);

        assertNotNull(token);
        assertTrue(token.length() > 20);

        // Validate token
        assertTrue(jwtUtils.validateToken(token));

        // Extract phone
        String extractedPhone = jwtUtils.getPhoneFromToken(token);
        assertEquals(phone, extractedPhone);
    }

    @Test
    void testValidateInvalidToken() {
        assertFalse(jwtUtils.validateToken("invalid.jwt.token"));
        assertFalse(jwtUtils.validateToken(""));
        assertFalse(jwtUtils.validateToken(null));
    }
}
