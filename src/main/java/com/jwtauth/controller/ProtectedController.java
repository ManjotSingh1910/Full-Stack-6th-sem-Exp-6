package com.jwtauth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ProtectedController — demonstrates routes that require a valid JWT token.
 * All routes here require the Authorization: Bearer <token> header.
 */
@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    /**
     * GET /api/protected/dashboard
     * Accessible by any authenticated user.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(Map.of(
                "message", "Welcome to your dashboard! You are authenticated.",
                "username", auth.getName(),
                "authorities", auth.getAuthorities().toString()
        ));
    }

    /**
     * GET /api/protected/profile
     * Returns the current user's profile info from the security context.
     */
    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> profile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(Map.of(
                "username", auth.getName(),
                "role", auth.getAuthorities().iterator().next().getAuthority(),
                "status", "active"
        ));
    }

    /**
     * GET /api/protected/admin
     * Only accessible by users with ROLE_ADMIN.
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> adminOnly() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(Map.of(
                "message", "Admin panel — restricted access granted.",
                "admin", auth.getName()
        ));
    }
}
