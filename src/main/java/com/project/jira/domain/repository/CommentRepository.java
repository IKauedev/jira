package com.project.jira.domain.repository;

import com.project.jira.domain.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByIssueId(String issueId);
    List<Comment> findByAuthor(String author);
}
