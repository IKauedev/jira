package com.project.integration.presentation.controller;

import com.project.integration.client.GitHubIntegrationClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/github")
@RequiredArgsConstructor
@Tag(name = "GitHub Integration", description = "API de integração com GitHub")
public class GitHubIntegrationController {

    private final GitHubIntegrationClient gitHubClient;

    @GetMapping("/users/{username}")
    @Operation(summary = "Obter usuário do GitHub", description = "Busca informações de um usuário do GitHub")
    public Mono<ResponseEntity<GitHubIntegrationClient.GitHubUser>> getUser(@PathVariable String username) {
        return gitHubClient.getUser(username)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> Mono.just(ResponseEntity.status(500).build()));
    }

    @GetMapping("/repos/{owner}/{repo}")
    @Operation(summary = "Obter repositório do GitHub", description = "Busca informações de um repositório do GitHub")
    public Mono<ResponseEntity<GitHubIntegrationClient.GitHubRepository>> getRepository(
            @PathVariable String owner,
            @PathVariable String repo) {
        return gitHubClient.getRepository(owner, repo)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> Mono.just(ResponseEntity.status(500).build()));
    }

    @GetMapping("/repos/{owner}/{repo}/issues")
    @Operation(summary = "Listar issues do repositório", description = "Lista todas as issues de um repositório")
    public Mono<ResponseEntity<List<GitHubIntegrationClient.GitHubIssue>>> getIssues(
            @PathVariable String owner,
            @PathVariable String repo) {
        return gitHubClient.getIssues(owner, repo)
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorResume(error -> Mono.just(ResponseEntity.status(500).build()));
    }
}
