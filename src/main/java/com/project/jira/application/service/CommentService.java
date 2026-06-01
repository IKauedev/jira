package com.project.jira.application.service;

import com.project.jira.domain.entity.Comment;
import com.project.jira.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment createComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public Comment getCommentById(String id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));
    }

    public List<Comment> getCommentsByIssueId(String issueId) {
        return commentRepository.findByIssueId(issueId);
    }

    public List<Comment> getCommentsByAuthor(String author) {
        return commentRepository.findByAuthor(author);
    }

    public Comment updateComment(String id, Comment commentDetails) {
        Comment comment = getCommentById(id);
        comment.setBody(commentDetails.getBody());
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }
}
