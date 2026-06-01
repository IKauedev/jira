package com.project.integration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "integration")
public class IntegrationProperties {

    private GitHub github;
    private GitLab gitlab;
    private Slack slack;
    private Email email;

    @Data
    public static class GitHub {
        private String baseUrl;
        private String token;
        private String apiVersion;
    }

    @Data
    public static class GitLab {
        private String baseUrl;
        private String token;
        private String apiVersion;
    }

    @Data
    public static class Slack {
        private String baseUrl;
        private String token;
        private String webhookUrl;
    }

    @Data
    public static class Email {
        private String host;
        private int port;
        private String username;
        private String password;
    }
}
