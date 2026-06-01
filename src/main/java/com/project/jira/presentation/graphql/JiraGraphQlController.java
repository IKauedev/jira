package com.project.jira.presentation.graphql;

import com.project.jira.application.dto.IssueDTO;
import com.project.jira.application.dto.ProjectDTO;
import com.project.jira.application.dto.UserResponse;
import com.project.jira.application.service.IssueService;
import com.project.jira.application.service.ProjectService;
import com.project.jira.application.service.UserService;
import com.project.jira.domain.entity.Issue;
import com.project.jira.domain.entity.Project;
import com.project.jira.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class JiraGraphQlController {

    private final ProjectService projectService;
    private final IssueService issueService;
    private final UserService userService;

    @QueryMapping
    public List<GraphQlProject> projects() {
        return projectService.getAllProjects().stream()
                .map(GraphQlProject::from)
                .toList();
    }

    @QueryMapping
    public GraphQlProject project(@Argument String id) {
        return GraphQlProject.from(projectService.getProjectById(id));
    }

    @QueryMapping
    public GraphQlProject projectByKey(@Argument String key) {
        return GraphQlProject.from(projectService.getProjectByKey(key));
    }

    @QueryMapping
    public List<GraphQlProject> projectsByLead(@Argument String lead) {
        return projectService.getProjectsByLead(lead).stream()
                .map(GraphQlProject::from)
                .toList();
    }

    @QueryMapping
    public List<GraphQlIssue> issues() {
        return issueService.getAllIssues().stream()
                .map(GraphQlIssue::from)
                .toList();
    }

    @QueryMapping
    public GraphQlIssue issue(@Argument String id) {
        return GraphQlIssue.from(issueService.getIssueById(id));
    }

    @QueryMapping
    public GraphQlIssue issueByKey(@Argument String key) {
        return GraphQlIssue.from(issueService.getIssueByKey(key));
    }

    @QueryMapping
    public List<GraphQlIssue> issuesByProjectId(@Argument String projectId) {
        return issueService.getIssuesByProjectId(projectId).stream()
                .map(GraphQlIssue::from)
                .toList();
    }

    @QueryMapping
    public List<GraphQlIssue> issuesByAssignee(@Argument String assignee) {
        return issueService.getIssuesByAssignee(assignee).stream()
                .map(GraphQlIssue::from)
                .toList();
    }

    @QueryMapping
    public List<GraphQlIssue> issuesByStatus(@Argument Issue.IssueStatus status) {
        return issueService.getIssuesByStatus(status).stream()
                .map(GraphQlIssue::from)
                .toList();
    }

    @QueryMapping
    public List<GraphQlIssue> issuesByProjectAndStatus(@Argument String projectId, @Argument Issue.IssueStatus status) {
        return issueService.getIssuesByProjectAndStatus(projectId, status).stream()
                .map(GraphQlIssue::from)
                .toList();
    }

    @QueryMapping
    public List<GraphQlUser> users() {
        return userService.getAllUsers().stream()
                .map(GraphQlUser::from)
                .toList();
    }

    @QueryMapping
    public GraphQlUser user(@Argument String id) {
        return GraphQlUser.from(userService.getUserById(id));
    }

    @QueryMapping
    public GraphQlUser userByUsername(@Argument String username) {
        return GraphQlUser.from(userService.getUserByUsername(username));
    }

    @QueryMapping
    public GraphQlUser userByEmail(@Argument String email) {
        return GraphQlUser.from(userService.getUserByEmail(email));
    }

    @QueryMapping
    public List<GraphQlUser> usersByRole(@Argument User.UserRole role) {
        return userService.getUsersByRole(role).stream()
                .map(GraphQlUser::from)
                .toList();
    }

    public record GraphQlProject(
            String id,
            String key,
            String name,
            String description,
            String lead,
            String category,
            Project.ProjectType projectType,
            String createdAt,
            String updatedAt
    ) {
        public static GraphQlProject from(ProjectDTO project) {
            return new GraphQlProject(
                    project.getId(),
                    project.getKey(),
                    project.getName(),
                    project.getDescription(),
                    project.getLead(),
                    project.getCategory(),
                    project.getProjectType(),
                    formatDate(project.getCreatedAt()),
                    formatDate(project.getUpdatedAt())
            );
        }
    }

    public record GraphQlIssue(
            String id,
            String key,
            String projectId,
            String summary,
            String description,
            Issue.IssueType issueType,
            Issue.IssuePriority priority,
            Issue.IssueStatus status,
            String assignee,
            String reporter,
            List<String> labels,
            List<String> components,
            String createdAt,
            String updatedAt,
            String dueDate
    ) {
        public static GraphQlIssue from(IssueDTO issue) {
            return new GraphQlIssue(
                    issue.getId(),
                    issue.getKey(),
                    issue.getProjectId(),
                    issue.getSummary(),
                    issue.getDescription(),
                    issue.getIssueType(),
                    issue.getPriority(),
                    issue.getStatus(),
                    issue.getAssignee(),
                    issue.getReporter(),
                    issue.getLabels(),
                    issue.getComponents(),
                    formatDate(issue.getCreatedAt()),
                    formatDate(issue.getUpdatedAt()),
                    formatDate(issue.getDueDate())
            );
        }
    }

    public record GraphQlUser(
            String id,
            String username,
            String email,
            String fullName,
            String avatarUrl,
            User.UserRole role,
            boolean active,
            String createdAt,
            String updatedAt
    ) {
        public static GraphQlUser from(UserResponse user) {
            return new GraphQlUser(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFullName(),
                    user.getAvatarUrl(),
                    user.getRole(),
                    user.isActive(),
                    formatDate(user.getCreatedAt()),
                    formatDate(user.getUpdatedAt())
            );
        }
    }

    private static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.toString();
    }
}
