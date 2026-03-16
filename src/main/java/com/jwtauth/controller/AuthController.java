package com.jwtauth.controller;

import com.jwtauth.dto.AuthResponse;
import com.jwtauth.dto.LoginRequest;
import com.jwtauth.dto.RegisterRequest;
import com.jwtauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AuthController — handles user registration and login.
 * Routes under /api/auth/** are publicly accessible (no JWT required).
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/register
     * Register a new user and return a JWT token.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/login
     * Authenticate user and return a JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/logout
     * Client-side logout (JWT is stateless — client should discard the token).
     * For server-side invalidation, a token blacklist/Redis can be used.
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        // With stateless JWT, logout is handled client-side by discarding the token.
        // In production, add token to a blacklist (e.g., Redis) here.
        return ResponseEntity.ok(Map.of(
                "message", "Logout successful. Please discard your token on the client side.",
                "status", "success"
        ));
    }
}
