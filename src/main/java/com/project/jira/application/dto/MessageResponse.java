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
@Schema(description = "Resposta genérica com mensagem")
public class MessageResponse {

    @Schema(description = "Mensagem de resposta", example = "Operação realizada com sucesso")
    private String message;

    @Schema(description = "Se a operação foi bem-sucedida", example = "true")
    private boolean success;

    public static MessageResponse success(String message) {
        return new MessageResponse(message, true);
    }

    public static MessageResponse error(String message) {
        return new MessageResponse(message, false);
    }
}
