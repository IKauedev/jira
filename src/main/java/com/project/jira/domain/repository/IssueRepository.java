package com.project.jira.domain.repository;

import com.project.jira.domain.entity.Issue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends MongoRepository<Issue, String> {
    Optional<Issue> findByKey(String key);
    List<Issue> findByProjectId(String projectId);
    List<Issue> findByAssignee(String assignee);
    List<Issue> findByStatus(Issue.IssueStatus status);
    List<Issue> findByProjectIdAndStatus(String projectId, Issue.IssueStatus status);
}
