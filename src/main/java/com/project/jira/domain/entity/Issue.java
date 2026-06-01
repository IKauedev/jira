package com.project.jira.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "issues")
public class Issue {

    @Id
    private String id;
    private String key;
    private String projectId;
    private String summary;
    private String description;
    private IssueType issueType;
    private IssuePriority priority;
    private IssueStatus status;
    private String assignee;
    private String reporter;
    private List<String> labels;
    private List<String> components;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;

    public enum IssueType {
        BUG, FEATURE, TASK, IMPROVEMENT, EPIC, SUBTASK
    }

    public enum IssuePriority {
        LOWEST, LOW, MEDIUM, HIGH, HIGHEST
    }

    public enum IssueStatus {
        BACKLOG, TO_DO, IN_PROGRESS, IN_REVIEW, DONE, CLOSED
    }
}
