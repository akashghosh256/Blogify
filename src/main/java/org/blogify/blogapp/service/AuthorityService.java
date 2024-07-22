package org.blogify.blogapp.service;

import org.blogify.blogapp.model.Authority;

import java.util.Optional;

public interface AuthorityService {

    Optional<Authority> findByAuthority(String authority);

    Authority save(Authority authority);

    void deleteById(Long id);
}
