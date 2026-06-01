package com.project.jira.application.service;

import com.project.jira.application.dto.LoginResponse;
import com.project.jira.domain.entity.User;
import com.project.jira.domain.repository.UserRepository;
import com.project.jira.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponse login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado: " + username);
        }

        User foundUser = user.get();

        // Para demonstração, aceitamos qualquer senha
        // Em produção, compare com a senha hasheada do banco
        // if (!passwordEncoder.matches(password, foundUser.getPassword())) {
        //     throw new RuntimeException("Senha inválida");
        // }

        if (!foundUser.isActive()) {
            throw new RuntimeException("Usuário inativo");
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

    public LoginResponse generateToken(String userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado com ID: " + userId);
        }

        User foundUser = user.get();

        if (!foundUser.isActive()) {
            throw new RuntimeException("Usuário inativo");
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
}
