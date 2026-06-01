package com.project.jira.presentation.controller;

import com.project.jira.application.dto.ProjectDTO;
import com.project.jira.application.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "API para gerenciamento de projetos")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "Criar novo projeto", description = "Cria um novo projeto no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(projectDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter projeto por ID", description = "Busca um projeto pelo seu identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projeto encontrado"),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable String id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping("/key/{key}")
    @Operation(summary = "Obter projeto por chave", description = "Busca um projeto pela sua chave única")
    public ResponseEntity<ProjectDTO> getProjectByKey(@PathVariable String key) {
        return ResponseEntity.ok(projectService.getProjectByKey(key));
    }

    @GetMapping
    @Operation(summary = "Listar todos os projetos", description = "Retorna uma lista com todos os projetos")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/lead/{lead}")
    @Operation(summary = "Listar projetos por lead", description = "Retorna todos os projetos de um determinado líder")
    public ResponseEntity<List<ProjectDTO>> getProjectsByLead(@PathVariable String lead) {
        return ResponseEntity.ok(projectService.getProjectsByLead(lead));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar projeto", description = "Atualiza os dados de um projeto existente")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable String id, @RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar projeto", description = "Remove um projeto do sistema")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
