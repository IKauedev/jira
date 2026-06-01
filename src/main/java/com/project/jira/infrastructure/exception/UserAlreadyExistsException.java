package com.project.jira.infrastructure.exception;

public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    
    public static UserAlreadyExistsException byUsername(String username) {
        return new UserAlreadyExistsException("Usuário já existe com o username: " + username);
    }
    
    public static UserAlreadyExistsException byEmail(String email) {
        return new UserAlreadyExistsException("Usuário já existe com o email: " + email);
    }
}
