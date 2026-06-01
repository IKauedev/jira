package com.project.jira.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "projects")
public class Project {

    @Id
    private String id;
    private String key;
    private String name;
    private String description;
    private String lead;
    private String category;
    private ProjectType projectType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum ProjectType {
        SOFTWARE, SERVICE_MANAGEMENT, BUSINESS
    }
}
