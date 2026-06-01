package com.project.integration.service;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;

@Service
public class IntegrationService {

    private final Map<String, IntegrationProvider> providers = new HashMap<>();

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public void registerProvider(String name, IntegrationProvider provider) {
        providers.put(name, provider);
    }

    public IntegrationProvider getProvider(String name) {
        return providers.getOrDefault(name, null);
    }

    public <T> Mono<T> executeIntegration(String providerName, String operation, Class<T> responseType) {
        IntegrationProvider provider = getProvider(providerName);
        if (provider == null) {
            return Mono.error(new RuntimeException("Provider não encontrado: " + providerName));
        }

        return provider.execute(operation, responseType);
    }

    public interface IntegrationProvider {
        <T> Mono<T> execute(String operation, Class<T> responseType);
    }
}
