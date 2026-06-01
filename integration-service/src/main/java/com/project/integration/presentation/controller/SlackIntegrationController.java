package com.project.integration.presentation.controller;

import com.project.integration.client.SlackIntegrationClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/slack")
@RequiredArgsConstructor
@Tag(name = "Slack Integration", description = "API de integração com Slack")
public class SlackIntegrationController {

    private final SlackIntegrationClient slackClient;

    @PostMapping("/messages")
    @Operation(summary = "Enviar mensagem ao Slack", description = "Envia uma mensagem para um canal do Slack")
    public Mono<ResponseEntity<SlackIntegrationClient.SlackMessage>> sendMessage(
            @RequestParam String channel,
            @RequestParam String message) {
        return slackClient.sendMessage(channel, message)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> Mono.just(ResponseEntity.status(500).build()));
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Obter usuário do Slack", description = "Busca informações de um usuário do Slack")
    public Mono<ResponseEntity<SlackIntegrationClient.SlackUser>> getUser(@PathVariable String userId) {
        return slackClient.getUser(userId)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> Mono.just(ResponseEntity.status(500).build()));
    }

    @GetMapping("/channels/{channelId}")
    @Operation(summary = "Obter canal do Slack", description = "Busca informações de um canal do Slack")
    public Mono<ResponseEntity<SlackIntegrationClient.SlackChannel>> getChannel(@PathVariable String channelId) {
        return slackClient.getChannel(channelId)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> Mono.just(ResponseEntity.status(500).build()));
    }
}
