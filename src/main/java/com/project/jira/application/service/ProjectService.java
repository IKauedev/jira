package com.project.jira.application.service;

import com.project.jira.domain.entity.Project;
import com.project.jira.domain.repository.ProjectRepository;
import com.project.jira.application.dto.ProjectDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = Project.builder()
                .key(projectDTO.getKey())
                .name(projectDTO.getName())
                .description(projectDTO.getDescription())
                .lead(projectDTO.getLead())
                .category(projectDTO.getCategory())
                .projectType(projectDTO.getProjectType())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Project savedProject = projectRepository.save(project);
        return mapToDTO(savedProject);
    }

    public ProjectDTO getProjectById(String id) {
        return projectRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
    }

    public ProjectDTO getProjectByKey(String key) {
        return projectRepository.findByKey(key)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
    }

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<ProjectDTO> getProjectsByLead(String lead) {
        return projectRepository.findByLead(lead)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public ProjectDTO updateProject(String id, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setLead(projectDTO.getLead());
        project.setCategory(projectDTO.getCategory());
        project.setUpdatedAt(LocalDateTime.now());

        Project updatedProject = projectRepository.save(project);
        return mapToDTO(updatedProject);
    }

    public void deleteProject(String id) {
        projectRepository.deleteById(id);
    }

    private ProjectDTO mapToDTO(Project project) {
        return ProjectDTO.builder()
                .id(project.getId())
                .key(project.getKey())
                .name(project.getName())
                .description(project.getDescription())
                .lead(project.getLead())
                .category(project.getCategory())
                .projectType(project.getProjectType())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
