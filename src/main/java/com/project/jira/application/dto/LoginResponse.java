package com.project.jira.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String type;
    private long expiresIn;
    private String username;
    private String userId;
    private String email;
    private String role;

    public LoginResponse(String token, String username, String userId, String email, String role, long expiresIn) {
        this.token = token;
        this.type = "Bearer";
        this.expiresIn = expiresIn;
        this.username = username;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }
}
