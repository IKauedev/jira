package com.project.jira.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
@Tag(name = "Health", description = "Endpoints de saúde da aplicação")
public class HealthController {

    @Value("${app.version}")
    private String version;

    @Value("${app.title}")
    private String title;

    @Value("${app.environment}")
    private String environment;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @GetMapping
    @Operation(summary = "Health check", description = "Verifica se a aplicação está funcionando")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", title);
        response.put("version", version);
        response.put("environment", environment);
        response.put("profile", activeProfile);
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    @Operation(summary = "Informações da Aplicação", description = "Retorna informações detalhadas sobre a aplicação e ambiente")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", title);
        response.put("version", version);
        response.put("environment", environment);
        response.put("profile", activeProfile);
        response.put("runtime", Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB");
        response.put("javaVersion", System.getProperty("java.version"));
        response.put("osName", System.getProperty("os.name"));
        response.put("osVersion", System.getProperty("os.version"));
        return ResponseEntity.ok(response);
    }
}
