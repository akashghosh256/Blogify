package org.blogify.blogapp.service;

import org.blogify.blogapp.model.Post;

import java.util.Collection;
import java.util.Optional;

public interface PostService {

    Optional<Post> findById(Long id);

    Collection<Post> findAll();

    Post save(Post post);
    Optional<Post> getById(Long id);
    void deleteById(Long id);
}
