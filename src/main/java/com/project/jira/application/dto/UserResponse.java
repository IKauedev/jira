package com.project.jira.application.dto;

import com.project.jira.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados do usuário (sem senha)")
public class UserResponse {

    @Schema(description = "ID único do usuário", example = "66666666666666666666661")
    private String id;

    @Schema(description = "Nome de usuário", example = "joao.silva")
    private String username;

    @Schema(description = "Email do usuário", example = "joao.silva@example.com")
    private String email;

    @Schema(description = "Nome completo", example = "João Silva")
    private String fullName;

    @Schema(description = "URL do avatar")
    private String avatarUrl;

    @Schema(description = "Papel do usuário", example = "DEVELOPER")
    private User.UserRole role;

    @Schema(description = "Se o usuário está ativo", example = "true")
    private boolean active;

    @Schema(description = "Data de criação")
    private LocalDateTime createdAt;

    @Schema(description = "Data da última atualização")
    private LocalDateTime updatedAt;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
