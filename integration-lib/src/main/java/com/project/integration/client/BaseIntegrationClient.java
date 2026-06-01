package com.project.integration.client;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.JsonNode;

@RequiredArgsConstructor
public abstract class BaseIntegrationClient {

    protected final WebClient webClient;

    protected <T> Mono<T> get(String path, Class<T> responseType) {
        return webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(responseType)
                .doOnError(this::handleError);
    }

    protected <T> Mono<T> post(String path, Object body, Class<T> responseType) {
        return webClient.post()
                .uri(path)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType)
                .doOnError(this::handleError);
    }

    protected <T> Mono<T> put(String path, Object body, Class<T> responseType) {
        return webClient.put()
                .uri(path)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType)
                .doOnError(this::handleError);
    }

    protected <T> Mono<T> delete(String path, Class<T> responseType) {
        return webClient.delete()
                .uri(path)
                .retrieve()
                .bodyToMono(responseType)
                .doOnError(this::handleError);
    }

    protected Mono<Void> handleError(Throwable error) {
        if (error instanceof WebClientResponseException) {
            WebClientResponseException webError = (WebClientResponseException) error;
            System.err.println("Integration Error: " + webError.getRawStatusCode() + " - " + webError.getResponseBodyAsString());
        } else {
            System.err.println("Integration Error: " + error.getMessage());
        }
        return Mono.error(error);
    }
}
