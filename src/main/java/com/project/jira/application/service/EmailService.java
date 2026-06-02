package com.project.jira.application.service;

public interface EmailService {

    void sendPasswordResetEmail(String recipientEmail, String resetToken);
}
