package com.project.jira.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.project.jira.domain.entity.Issue;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueDTO {
    private String id;
    private String key;
    private String projectId;
    private String summary;
    private String description;
    private Issue.IssueType issueType;
    private Issue.IssuePriority priority;
    private Issue.IssueStatus status;
    private String assignee;
    private String reporter;
    private List<String> labels;
    private List<String> components;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;
}
