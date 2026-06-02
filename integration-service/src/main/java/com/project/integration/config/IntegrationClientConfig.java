package com.project.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import com.project.integration.client.GitHubIntegrationClient;
import com.project.integration.client.SlackIntegrationClient;
import com.project.integration.config.IntegrationProperties.GitHub;
import com.project.integration.config.IntegrationProperties.Slack;

@Configuration
public class IntegrationClientConfig {

    private final IntegrationProperties integrationProperties;

    public IntegrationClientConfig(IntegrationProperties integrationProperties) {
        this.integrationProperties = integrationProperties;
    }

    @Bean
    public WebClient githubWebClient() {
        GitHub github = integrationProperties.getGithub();
        WebClient.Builder builder = WebClient.builder()
                .baseUrl(github.getBaseUrl())
                .defaultHeader("Accept", "application/vnd.github.v3+json");

        if (StringUtils.hasText(github.getToken())) {
            builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + github.getToken());
        }

        return builder.build();
    }

    @Bean
    public WebClient slackWebClient() {
        Slack slack = integrationProperties.getSlack();
        WebClient.Builder builder = WebClient.builder()
                .baseUrl(slack.getBaseUrl())
                .defaultHeader("Content-Type", "application/json");

        if (StringUtils.hasText(slack.getToken())) {
            builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + slack.getToken());
        }

        return builder.build();
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
