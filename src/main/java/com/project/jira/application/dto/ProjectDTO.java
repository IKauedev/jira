package com.project.jira.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.project.jira.domain.entity.Project;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private String id;
    private String key;
    private String name;
    private String description;
    private String lead;
    private String category;
    private Project.ProjectType projectType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
