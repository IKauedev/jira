package com.project.jira.application.service;

import com.project.jira.domain.entity.User;
import com.project.jira.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserById(String id) {
        Optional<User> userOpt = userRepository.findById(id);
        userOpt.ifPresent(user -> user.setPassword(null));
        return userOpt.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public User getUserByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        userOpt.ifPresent(user -> user.setPassword(null));
        return userOpt.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    public Optional<User> getUsersByRole(User.UserRole role) {
        Optional<User> usersOpt = userRepository.findByRole(role);
        usersOpt.ifPresent(user -> user.setPassword(null));
        return usersOpt.isEmpty() ? Optional.ofNullable(usersOpt.orElseThrow(() -> new RuntimeException("Usu\u00E1rio n\u00E3o encontrado com permiss\u00E3o informada"))) : usersOpt;
    }

    public User updateUser(String id, User userDetails) {
        User user = getUserById(id);
        user.setEmail(userDetails.getEmail());
        user.setFullName(userDetails.getFullName());
        user.setRole(userDetails.getRole());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
