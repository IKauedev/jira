package com.project.jira.infrastructure.exception;

public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException() {
        super("Usuário não encontrado");
    }
    
    public UserNotFoundException(String message) {
        super(message);
    }
}
