package com.project.jira.application.service;

import com.project.jira.application.dto.LoginResponse;
import com.project.jira.domain.entity.PasswordResetToken;
import com.project.jira.domain.entity.User;
import com.project.jira.domain.repository.PasswordResetTokenRepository;
import com.project.jira.domain.repository.UserRepository;
import com.project.jira.infrastructure.exception.InvalidCredentialsException;
import com.project.jira.infrastructure.exception.InvalidTokenException;
import com.project.jira.infrastructure.exception.UserNotFoundException;
import com.project.jira.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    @Value("${app.password-reset.expiration-hours:24}")
    private int passwordResetExpirationHours;

    public LoginResponse login(String username, String password) {
        User foundUser = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(password, foundUser.getPassword())) {
            log.warn("Tentativa de login com senha inválida para usuário: {}", username);
            throw new InvalidCredentialsException();
        }

        if (!foundUser.isActive()) {
            log.warn("Tentativa de login para usuário inativo: {}", username);
            throw new InvalidCredentialsException("Usuário inativo");
        }

        log.info("Login realizado com sucesso para usuário: {}", username);

        String token = jwtUtil.generateToken(
                foundUser.getUsername(),
                foundUser.getId(),
                foundUser.getEmail(),
                foundUser.getRole().toString()
        );

        return new LoginResponse(
                token,
                foundUser.getUsername(),
                foundUser.getId(),
                foundUser.getEmail(),
                foundUser.getRole().toString(),
                jwtExpiration
        );
    }

    public LoginResponse generateToken(String userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com ID: " + userId));

        if (!foundUser.isActive()) {
            throw new InvalidCredentialsException("Usuário inativo");
        }

        String token = jwtUtil.generateToken(
                foundUser.getUsername(),
                foundUser.getId(),
                foundUser.getEmail(),
                foundUser.getRole().toString()
        );

        return new LoginResponse(
                token,
                foundUser.getUsername(),
                foundUser.getId(),
                foundUser.getEmail(),
                foundUser.getRole().toString(),
                jwtExpiration
        );
    }

    public boolean validateToken(String token) {
        return jwtUtil.isTokenValid(token);
    }

    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token);
    }

    public String extractUserId(String token) {
        return jwtUtil.extractUserId(token);
    }

    public LoginResponse generateTokenByUsername(String username) {
        User foundUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + username));

        if (!foundUser.isActive()) {
            throw new InvalidCredentialsException("Usuário inativo");
        }

        String token = jwtUtil.generateToken(
                foundUser.getUsername(),
                foundUser.getId(),
                foundUser.getEmail(),
                foundUser.getRole().toString()
        );

        return new LoginResponse(
                token,
                foundUser.getUsername(),
                foundUser.getId(),
                foundUser.getEmail(),
                foundUser.getRole().toString(),
                jwtExpiration
        );
    }

    public String createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com email: " + email));

        passwordResetTokenRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();
        
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .userId(user.getId())
                .email(email)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(passwordResetExpirationHours))
                .used(false)
                .build();

        passwordResetTokenRepository.save(resetToken);
        
        log.info("Token de redefinição de senha criado para usuário: {}", user.getUsername());
        
        return token;
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new InvalidTokenException("Token de redefinição inválido"));

        if (resetToken.isExpired()) {
            throw new InvalidTokenException("Token de redefinição expirado");
        }

        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new UserNotFoundException());

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        log.info("Senha redefinida com sucesso para usuário: {}", user.getUsername());
    }

    public void changePassword(String userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Senha atual incorreta");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Senha alterada com sucesso para usuário: {}", user.getUsername());
    }

    public boolean validatePasswordResetToken(String token) {
        return passwordResetTokenRepository.findByTokenAndUsedFalse(token)
                .map(PasswordResetToken::isValid)
                .orElse(false);
    }
}
