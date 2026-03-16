package com.jwtauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type;
    private String username;
    private String message;

    public AuthResponse(String token, String username) {
        this.token = token;
        this.type = "Bearer";
        this.username = username;
        this.message = "Authentication successful";
    }
}
