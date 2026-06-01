package com.project.jira.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados para alteração de senha")
public class ChangePasswordRequest {

    @NotBlank(message = "Senha atual é obrigatória")
    @Schema(description = "Senha atual do usuário", example = "SenhaAtual123!")
    private String currentPassword;

    @NotBlank(message = "Nova senha é obrigatória")
    @Size(min = 8, max = 100, message = "Senha deve ter entre 8 e 100 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Senha deve conter pelo menos: uma letra maiúscula, uma minúscula, um número e um caractere especial")
    @Schema(description = "Nova senha", example = "NovaSenha123!")
    private String newPassword;

    @NotBlank(message = "Confirmação de senha é obrigatória")
    @Schema(description = "Confirmação da nova senha", example = "NovaSenha123!")
    private String confirmPassword;
}
