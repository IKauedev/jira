package com.project.jira.presentation.controller;

import com.project.jira.domain.entity.Comment;
import com.project.jira.application.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "API para gerenciamento de comentários")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Criar novo comentário", description = "Cria um novo comentário em uma issue")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(comment));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter comentário por ID", description = "Busca um comentário pelo seu identificador único")
    public ResponseEntity<Comment> getCommentById(@PathVariable String id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @GetMapping("/issue/{issueId}")
    @Operation(summary = "Listar comentários por issue", description = "Retorna todos os comentários de uma issue")
    public ResponseEntity<List<Comment>> getCommentsByIssueId(@PathVariable String issueId) {
        return ResponseEntity.ok(commentService.getCommentsByIssueId(issueId));
    }

    @GetMapping("/author/{author}")
    @Operation(summary = "Listar comentários por autor", description = "Retorna todos os comentários de um autor")
    public ResponseEntity<List<Comment>> getCommentsByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(commentService.getCommentsByAuthor(author));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar comentário", description = "Atualiza os dados de um comentário existente")
    public ResponseEntity<Comment> updateComment(@PathVariable String id, @RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.updateComment(id, comment));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar comentário", description = "Remove um comentário do sistema")
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
