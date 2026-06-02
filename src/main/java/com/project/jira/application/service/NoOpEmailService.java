package com.project.jira.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(name = "app.email.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpEmailService implements EmailService {

    @Override
    public void sendPasswordResetEmail(String recipientEmail, String resetToken) {
        log.debug("Envio de email desabilitado. Token de reset gerado para {}", recipientEmail);
    }
}
