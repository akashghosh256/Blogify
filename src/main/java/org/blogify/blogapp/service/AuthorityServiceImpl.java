package org.blogify.blogapp.service;

import org.blogify.blogapp.model.Authority;
import org.blogify.blogapp.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Optional<Authority> findByAuthority(String authority) {
        return authorityRepository.findByAuthority(authority);
    }

    @Override
    public Authority save(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Override
    public void deleteById(Long id) {
        authorityRepository.deleteById(id);
    }
}
