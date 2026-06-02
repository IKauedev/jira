package com.project.jira.presentation.controller;

import com.project.jira.application.dto.*;
import com.project.jira.application.service.AuthService;
import com.project.jira.application.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "API para autenticação, gerenciamento de tokens JWT e senhas")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @Value("${app.password-reset.include-token-in-response:false}")
    private boolean includePasswordResetTokenInResponse;

    @PostMapping("/login")
    @Operation(summary = "Login e gerar token JWT", description = "Realiza login com credenciais e retorna um token JWT Bearer")
    @SecurityRequirements
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/token/{userId}")
    @Operation(summary = "Gerar token para um usuário (Admin)", description = "Gera um novo token JWT para um usuário específico. Requer autenticação de administrador.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token gerado com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<LoginResponse> generateToken(
            @Parameter(description = "ID do usuário", required = true, example = "66666666666666666666661")
            @PathVariable String userId) {
        LoginResponse response = authService.generateToken(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validar token JWT", description = "Valida se um token JWT é válido")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token válido",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Token inválido ou não fornecido")
    })
    public ResponseEntity<MessageResponse> validateToken(
            @Parameter(description = "Header Authorization com Bearer token", required = true)
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(MessageResponse.error("Token não fornecido"));
        }

        String token = authHeader.substring(7);
        if (authService.validateToken(token)) {
            String username = authService.extractUsername(token);
            return ResponseEntity.ok(MessageResponse.success("Token válido para o usuário: " + username));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(MessageResponse.error("Token inválido"));
        }
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Solicitar redefinição de senha", description = "Envia um token de redefinição de senha para o email informado")
    @SecurityRequirements
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token de redefinição criado (em produção, seria enviado por email)",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Email não encontrado")
    })
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String token = authService.createPasswordResetToken(request.getEmail());
        emailService.sendPasswordResetEmail(request.getEmail(), token);

        String message = "Se o email estiver cadastrado, você receberá as instruções de redefinição.";
        if (includePasswordResetTokenInResponse) {
            message += " Token (dev): " + token;
        }

        return ResponseEntity.ok(MessageResponse.success(message));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Redefinir senha com token", description = "Redefine a senha usando o token recebido por email")
    @SecurityRequirements
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Senha redefinida com sucesso",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Token inválido ou senhas não conferem")
    })
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("As senhas não conferem"));
        }

        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(MessageResponse.success("Senha redefinida com sucesso"));
    }

    @GetMapping("/reset-password/validate")
    @Operation(summary = "Validar token de redefinição", description = "Verifica se um token de redefinição de senha é válido")
    @SecurityRequirements
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resultado da validação",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))
            )
    })
    public ResponseEntity<MessageResponse> validateResetToken(
            @Parameter(description = "Token de redefinição", required = true)
            @RequestParam String token) {
        boolean valid = authService.validatePasswordResetToken(token);
        if (valid) {
            return ResponseEntity.ok(MessageResponse.success("Token válido"));
        } else {
            return ResponseEntity.ok(MessageResponse.error("Token inválido ou expirado"));
        }
    }

    @PostMapping("/change-password")
    @Operation(summary = "Alterar senha", description = "Altera a senha do usuário autenticado")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Senha alterada com sucesso",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Senhas não conferem ou senha atual incorreta"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado")
    })
    public ResponseEntity<MessageResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @RequestHeader("Authorization") String authHeader) {
        
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("As senhas não conferem"));
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(MessageResponse.error("Token não fornecido"));
        }

        String token = authHeader.substring(7);
        if (!authService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(MessageResponse.error("Token inválido"));
        }

        String userId = authService.extractUserId(token);
        authService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok(MessageResponse.success("Senha alterada com sucesso"));
    }

    @GetMapping("/me")
    @Operation(summary = "Obter dados do usuário autenticado", description = "Retorna os dados do usuário atualmente autenticado")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados do usuário",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Nao autenticado")
    })
    public ResponseEntity<LoginResponse> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        if (!authService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authService.extractUsername(token);
        LoginResponse response = authService.generateTokenByUsername(username);
        return ResponseEntity.ok(response);
    }
}
