package com.project.jira.domain.repository;

import com.project.jira.domain.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findAllByRole(User.UserRole role);
    
    List<User> findAllByActive(boolean active);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
