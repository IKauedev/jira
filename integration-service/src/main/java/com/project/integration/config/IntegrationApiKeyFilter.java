package com.project.integration.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class IntegrationApiKeyFilter extends OncePerRequestFilter {

    @Value("${integration.security.apiKey:}")
    private String apiKey;

    @Value("${integration.security.headerName:X-Integration-Api-Key}")
    private String headerName;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (!requiresApiKey(request) || !StringUtils.hasText(apiKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        String providedApiKey = request.getHeader(headerName);
        if (apiKey.equals(providedApiKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"message\":\"Chave de integracao invalida ou ausente\"}");
    }

    private boolean requiresApiKey(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/v1/");
    }
}
