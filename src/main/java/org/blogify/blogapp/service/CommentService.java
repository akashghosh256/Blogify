package org.blogify.blogapp.service;

import org.blogify.blogapp.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Comment save(Comment comment);

    void delete(Long id);

    Optional<Comment> findById(Long id);

    List<Comment> findAll();
}
