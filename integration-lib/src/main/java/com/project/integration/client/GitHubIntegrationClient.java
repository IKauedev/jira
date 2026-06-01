package com.project.integration.client;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class GitHubIntegrationClient extends BaseIntegrationClient {
    public GitHubIntegrationClient(WebClient webClient) {
        super(webClient);
    }

    public Mono<GitHubUser> getUser(String username) {
        return get("/users/" + username, GitHubUser.class);
    }

    public Mono<GitHubRepository> getRepository(String owner, String repo) {
        return get("/repos/" + owner + "/" + repo, GitHubRepository.class);
    }

    public Flux<GitHubIssue> getIssues(String owner, String repo) {
        return webClient.get()
                .uri("/repos/" + owner + "/" + repo + "/issues")
                .retrieve()
                .bodyToFlux(GitHubIssue.class);
    }

    @Data
    @Builder
    public static class GitHubUser {
        private String login;
        private Long id;
        private String name;
        private String bio;
        private int publicRepos;
    }

    @Data
    @Builder
    public static class GitHubRepository {
        private Long id;
        private String name;
        private String fullName;
        private String description;
        private boolean isPrivate;
        private String url;
        private int stargazersCount;
    }

    @Data
    @Builder
    public static class GitHubIssue {
        private Long id;
        private String title;
        private String body;
        private String state;
        private String url;
    }
}
