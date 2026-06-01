package com.project.jira.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Resposta de autenticação com token JWT")
public class LoginResponse {

    @Schema(description = "Token JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Tipo do token", example = "Bearer")
    private String type;

    @Schema(description = "Tempo de expiração em milissegundos", example = "86400000")
    private long expiresIn;

    @Schema(description = "Nome de usuário", example = "admin")
    private String username;

    @Schema(description = "ID do usuário", example = "66666666666666666666661")
    private String userId;

    @Schema(description = "E-mail do usuário", example = "admin@jira.com")
    private String email;

    @Schema(description = "Papel do usuário", example = "ADMIN")
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
