package com.project.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import com.project.integration.client.GitHubIntegrationClient;
import com.project.integration.client.SlackIntegrationClient;

@Configuration
public class IntegrationClientConfig {

    @Bean
    public WebClient githubWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader("Accept", "application/vnd.github.v3+json")
                .build();
    }

    @Bean
    public WebClient slackWebClient() {
        return WebClient.builder()
                .baseUrl("https://slack.com/api")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean
    public GitHubIntegrationClient gitHubIntegrationClient() {
        return new GitHubIntegrationClient(githubWebClient());
    }

    @Bean
    public SlackIntegrationClient slackIntegrationClient() {
        return new SlackIntegrationClient(slackWebClient());
    }
}
