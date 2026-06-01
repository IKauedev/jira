package com.project.jira.domain.repository;

import com.project.jira.domain.entity.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
    Optional<Project> findByKey(String key);
    List<Project> findByLead(String lead);
    List<Project> findByCategory(String category);
}
