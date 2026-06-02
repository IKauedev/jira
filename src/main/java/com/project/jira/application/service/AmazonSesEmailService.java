package com.project.jira.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.email.enabled", havingValue = "true")
public class AmazonSesEmailService implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String from;

    @Value("${app.password-reset.base-url}")
    private String passwordResetBaseUrl;

    @Override
    public void sendPasswordResetEmail(String recipientEmail, String resetToken) {
        String resetUrl = UriComponentsBuilder.fromUriString(passwordResetBaseUrl)
                .queryParam("token", resetToken)
                .build()
                .toUriString();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(recipientEmail);
        message.setSubject("Redefinicao de senha");
        message.setText("""
                Recebemos uma solicitacao para redefinir sua senha.

                Acesse o link abaixo para criar uma nova senha:
                %s

                Se voce nao solicitou essa alteracao, ignore este email.
                """.formatted(resetUrl));

        mailSender.send(message);
        log.info("Email de redefinicao de senha enviado via Amazon SES para {}", recipientEmail);
    }
}
