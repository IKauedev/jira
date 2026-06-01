package com.project.jira.application.service;

import com.project.jira.domain.entity.Issue;
import com.project.jira.domain.repository.IssueRepository;
import com.project.jira.application.dto.IssueDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    public IssueDTO createIssue(IssueDTO issueDTO) {
        Issue issue = Issue.builder()
                .projectId(issueDTO.getProjectId())
                .summary(issueDTO.getSummary())
                .description(issueDTO.getDescription())
                .issueType(issueDTO.getIssueType())
                .priority(issueDTO.getPriority())
                .status(issueDTO.getStatus() != null ? issueDTO.getStatus() : Issue.IssueStatus.TO_DO)
                .assignee(issueDTO.getAssignee())
                .reporter(issueDTO.getReporter())
                .labels(issueDTO.getLabels())
                .components(issueDTO.getComponents())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .dueDate(issueDTO.getDueDate())
                .build();

        Issue savedIssue = issueRepository.save(issue);
        return mapToDTO(savedIssue);
    }

    public IssueDTO getIssueById(String id) {
        return issueRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Issue não encontrada"));
    }

    public IssueDTO getIssueByKey(String key) {
        return issueRepository.findByKey(key)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Issue não encontrada"));
    }

    public List<IssueDTO> getAllIssues() {
        return issueRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<IssueDTO> getIssuesByProjectId(String projectId) {
        return issueRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<IssueDTO> getIssuesByAssignee(String assignee) {
        return issueRepository.findByAssignee(assignee)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<IssueDTO> getIssuesByStatus(Issue.IssueStatus status) {
        return issueRepository.findByStatus(status)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<IssueDTO> getIssuesByProjectAndStatus(String projectId, Issue.IssueStatus status) {
        return issueRepository.findByProjectIdAndStatus(projectId, status)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public IssueDTO updateIssue(String id, IssueDTO issueDTO) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Issue não encontrada"));

        issue.setSummary(issueDTO.getSummary());
        issue.setDescription(issueDTO.getDescription());
        issue.setPriority(issueDTO.getPriority());
        issue.setStatus(issueDTO.getStatus());
        issue.setAssignee(issueDTO.getAssignee());
        issue.setLabels(issueDTO.getLabels());
        issue.setDueDate(issueDTO.getDueDate());
        issue.setUpdatedAt(LocalDateTime.now());

        Issue updatedIssue = issueRepository.save(issue);
        return mapToDTO(updatedIssue);
    }

    public void deleteIssue(String id) {
        issueRepository.deleteById(id);
    }

    private IssueDTO mapToDTO(Issue issue) {
        return IssueDTO.builder()
                .id(issue.getId())
                .key(issue.getKey())
                .projectId(issue.getProjectId())
                .summary(issue.getSummary())
                .description(issue.getDescription())
                .issueType(issue.getIssueType())
                .priority(issue.getPriority())
                .status(issue.getStatus())
                .assignee(issue.getAssignee())
                .reporter(issue.getReporter())
                .labels(issue.getLabels())
                .components(issue.getComponents())
                .createdAt(issue.getCreatedAt())
                .updatedAt(issue.getUpdatedAt())
                .dueDate(issue.getDueDate())
                .build();
    }
}
