package org.blogify.blogapp.service;

import org.blogify.blogapp.model.Comment;

public interface CommentService {

    Comment save(Comment comment);

    void delete(Comment comment);

}
