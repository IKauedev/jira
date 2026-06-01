package com.project.jira.application.dto;

import com.project.jira.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados para atualização de usuário")
public class UpdateUserRequest {

    @Email(message = "Email deve ser válido")
    @Schema(description = "Email do usuário", example = "joao.silva@example.com")
    private String email;

    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Schema(description = "Nome completo do usuário", example = "João Silva")
    private String fullName;

    @Schema(description = "URL do avatar")
    private String avatarUrl;

    @Schema(description = "Papel do usuário no sistema", example = "DEVELOPER")
    private User.UserRole role;

    @Schema(description = "Se o usuário está ativo", example = "true")
    private Boolean active;
}
