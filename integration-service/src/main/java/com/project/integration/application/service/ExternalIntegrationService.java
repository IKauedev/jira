package com.project.integration.application.service;

import com.project.integration.client.GitHubIntegrationClient;
import com.project.integration.client.SlackIntegrationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExternalIntegrationService {

    private final GitHubIntegrationClient gitHubClient;
    private final SlackIntegrationClient slackClient;

    /**
     * Notifica no Slack sobre um novo repositório GitHub
     */
    public Mono<Void> notifyNewRepository(String channel, String owner, String repo) {
        return gitHubClient.getRepository(owner, repo)
                .flatMap(repository -> {
                    String message = String.format(
                        "🔗 Novo repositório: *%s*\n" +
                        "Descrição: %s\n" +
                        "⭐ Stars: %d",
                        repository.getFullName(),
                        repository.getDescription(),
                        repository.getStargazersCount()
                    );
                    return slackClient.sendMessage(channel, message);
                })
                .then();
    }

    /**
     * Sincroniza issues do GitHub para o Jira
     */
    public Mono<Void> syncGitHubIssuesToJira(String owner, String repo) {
        return gitHubClient.getIssues(owner, repo)
                .collectList()
                .flatMap(issues -> {
                    String message = String.format(
                        "📊 Sincronização: %d issues encontradas em %s/%s",
                        issues.size(),
                        owner,
                        repo
                    );
                    return slackClient.sendMessage("#jira", message);
                })
                .then();
    }

    /**
     * Busca issues abertas do GitHub
     */
    public Mono<Long> countOpenGitHubIssues(String owner, String repo) {
        return gitHubClient.getIssues(owner, repo)
                .filter(issue -> "open".equals(issue.getState()))
                .count();
    }
}
