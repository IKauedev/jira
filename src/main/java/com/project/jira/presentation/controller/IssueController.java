package com.project.jira.presentation.controller;

import com.project.jira.application.dto.IssueDTO;
import com.project.jira.application.service.IssueService;
import com.project.jira.domain.entity.Issue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/issues")
@RequiredArgsConstructor
@Tag(name = "Issues", description = "API para gerenciamento de issues/tarefas")
@SecurityRequirement(name = "bearerAuth")
public class IssueController {

    private final IssueService issueService;

    @PostMapping
    @Operation(summary = "Criar nova issue", description = "Cria uma nova issue no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Issue criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<IssueDTO> createIssue(@RequestBody IssueDTO issueDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(issueService.createIssue(issueDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter issue por ID", description = "Busca uma issue pelo seu identificador único")
    public ResponseEntity<IssueDTO> getIssueById(@PathVariable String id) {
        return ResponseEntity.ok(issueService.getIssueById(id));
    }

    @GetMapping("/key/{key}")
    @Operation(summary = "Obter issue por chave", description = "Busca uma issue pela sua chave única")
    public ResponseEntity<IssueDTO> getIssueByKey(@PathVariable String key) {
        return ResponseEntity.ok(issueService.getIssueByKey(key));
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Listar issues por projeto", description = "Retorna todas as issues de um projeto")
    public ResponseEntity<List<IssueDTO>> getIssuesByProjectId(@PathVariable String projectId) {
        return ResponseEntity.ok(issueService.getIssuesByProjectId(projectId));
    }

    @GetMapping("/assignee/{assignee}")
    @Operation(summary = "Listar issues por responsável", description = "Retorna todas as issues atribuídas a um usuário")
    public ResponseEntity<List<IssueDTO>> getIssuesByAssignee(@PathVariable String assignee) {
        return ResponseEntity.ok(issueService.getIssuesByAssignee(assignee));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar issues por status", description = "Retorna todas as issues com um determinado status")
    public ResponseEntity<List<IssueDTO>> getIssuesByStatus(@PathVariable Issue.IssueStatus status) {
        return ResponseEntity.ok(issueService.getIssuesByStatus(status));
    }

    @GetMapping("/project/{projectId}/status/{status}")
    @Operation(summary = "Listar issues por projeto e status", description = "Retorna issues de um projeto com um status específico")
    public ResponseEntity<List<IssueDTO>> getIssuesByProjectAndStatus(
            @PathVariable String projectId,
            @PathVariable Issue.IssueStatus status) {
        return ResponseEntity.ok(issueService.getIssuesByProjectAndStatus(projectId, status));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar issue", description = "Atualiza os dados de uma issue existente")
    public ResponseEntity<IssueDTO> updateIssue(@PathVariable String id, @RequestBody IssueDTO issueDTO) {
        return ResponseEntity.ok(issueService.updateIssue(id, issueDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar issue", description = "Remove uma issue do sistema")
    public ResponseEntity<Void> deleteIssue(@PathVariable String id) {
        issueService.deleteIssue(id);
        return ResponseEntity.noContent().build();
    }
}
