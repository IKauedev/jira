package com.project.jira.application.service;

import com.project.jira.domain.entity.Project;
import com.project.jira.domain.repository.ProjectRepository;
import com.project.jira.application.dto.ProjectDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private ProjectDTO projectDTO;
    private Project project;

    @BeforeEach
    public void setUp() {
        projectDTO = ProjectDTO.builder()
                .key("TEST")
                .name("Test Project")
                .description("Test Description")
                .lead("lead@test.com")
                .category("Development")
                .projectType(Project.ProjectType.SOFTWARE)
                .build();

        project = Project.builder()
                .id("1")
                .key("TEST")
                .name("Test Project")
                .description("Test Description")
                .lead("lead@test.com")
                .category("Development")
                .projectType(Project.ProjectType.SOFTWARE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void testCreateProject() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectDTO result = projectService.createProject(projectDTO);

        assertNotNull(result);
        assertEquals("TEST", result.getKey());
        assertEquals("Test Project", result.getName());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    public void testGetProjectById() {
        when(projectRepository.findById("1")).thenReturn(Optional.of(project));

        ProjectDTO result = projectService.getProjectById("1");

        assertNotNull(result);
        assertEquals("TEST", result.getKey());
        verify(projectRepository, times(1)).findById("1");
    }

    @Test
    public void testGetAllProjects() {
        List<Project> projects = Arrays.asList(project);
        when(projectRepository.findAll()).thenReturn(projects);

        List<ProjectDTO> result = projectService.getAllProjects();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateProject() {
        when(projectRepository.findById("1")).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectDTO result = projectService.updateProject("1", projectDTO);

        assertNotNull(result);
        verify(projectRepository, times(1)).findById("1");
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    public void testDeleteProject() {
        projectService.deleteProject("1");
        verify(projectRepository, times(1)).deleteById("1");
    }
}
