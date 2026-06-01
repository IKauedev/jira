package com.project.jira.infrastructure.exception;

public class InvalidCredentialsException extends AuthenticationException {
    
    public InvalidCredentialsException() {
        super("Credenciais inválidas");
    }
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
