package com.project.jira.config;

import com.project.jira.infrastructure.security.JwtAuthenticationFilter;
import com.project.jira.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\":\"Não autenticado\"}");
                }))
                .authorizeHttpRequests(auth -> auth
                        // Health check - público
                        .requestMatchers("/api/v1/health/**").permitAll()
                        
                        // Auth endpoints públicos (login, forgot-password, reset-password)
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/auth/forgot-password").permitAll()
                        .requestMatchers("/api/v1/auth/reset-password").permitAll()
                        .requestMatchers("/api/v1/auth/reset-password/validate").permitAll()
                        
                        // Auth endpoints protegidos
                        .requestMatchers("/api/v1/auth/token/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/auth/**").authenticated()
                        
                        // Registro de usuário - público (POST apenas)
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        
                        // Ativação/desativação de usuário - apenas ADMIN
                        .requestMatchers("/api/v1/users/*/activate").hasRole("ADMIN")
                        .requestMatchers("/api/v1/users/*/deactivate").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/*").hasRole("ADMIN")
                        
                        // Documentação - público
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        
                        // GraphQL - requer autenticação
                        .requestMatchers("/graphql").authenticated()
                        .requestMatchers("/graphiql").permitAll()
                        
                        // Demais endpoints de API requerem autenticação
                        .requestMatchers("/api/v1/**").authenticated()
                        
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
