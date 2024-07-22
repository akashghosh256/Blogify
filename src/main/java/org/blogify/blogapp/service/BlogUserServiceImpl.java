package org.blogify.blogapp.service;

import org.blogify.blogapp.model.Authority;
import org.blogify.blogapp.model.BlogUser;
import org.blogify.blogapp.repository.AuthorityRepository;
import org.blogify.blogapp.repository.BlogUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class BlogUserServiceImpl implements BlogUserService {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final BCryptPasswordEncoder bcryptEncoder;
    private final BlogUserRepository blogUserRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public BlogUserServiceImpl(BCryptPasswordEncoder bcryptEncoder, BlogUserRepository blogUserRepository, AuthorityRepository authorityRepository) {
        this.bcryptEncoder = bcryptEncoder;
        this.blogUserRepository = blogUserRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return blogUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
    }

    @Override
    public Optional<BlogUser> findByUsername(String username) {
        return blogUserRepository.findByUsername(username);
    }

    @Override
    public BlogUser saveNewBlogUser(BlogUser blogUser) throws RoleNotFoundException {
        // Encode plaintext password
        blogUser.setPassword(bcryptEncoder.encode(blogUser.getPassword()));
        // Set account to enabled by default
        blogUser.setEnabled(true);
        // Set default Authority/Role to new blog user
        Authority authority = authorityRepository.findByAuthority(DEFAULT_ROLE)
                .orElseThrow(() -> new RoleNotFoundException("Default role not found for blog user with username " + blogUser.getUsername()));

        Collection<Authority> authorities = Collections.singletonList(authority);
        blogUser.setAuthorities(authorities);

        return blogUserRepository.save(blogUser);
    }
}
