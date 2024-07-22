package org.blogify.blogapp.controller;

import org.blogify.blogapp.model.BlogUser;
import org.blogify.blogapp.model.Post;
import org.blogify.blogapp.service.BlogUserService;
import org.blogify.blogapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@SessionAttributes("post")
public class PostController {

    private final PostService postService;
    private final BlogUserService blogUserService;

    @Autowired
    public PostController(PostService postService, BlogUserService blogUserService) {
        this.postService = postService;
        this.blogUserService = blogUserService;
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        // Get post by id
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);

            // Get current username from security context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String authUsername = auth != null ? auth.getName() : "anonymousUser";

            // Check if current user is the owner of the post
            if (authUsername.equals(post.getUser().getUsername())) {
                model.addAttribute("isOwner", true);
            }
            return "post";
        } else {
            return "404"; // Ensure you have a proper 404 error page
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/createNewPost")
    public String createNewPost(Model model) {
        // Get current username from security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth != null ? auth.getName() : "anonymousUser";

        Optional<BlogUser> optionalBlogUser = blogUserService.findByUsername(authUsername);
        if (optionalBlogUser.isPresent()) {
            Post post = new Post();
            post.setUser(optionalBlogUser.get());
            model.addAttribute("post", post);
            return "postForm";
        } else {
            return "error"; // Ensure you have a proper error page
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/createNewPost")
    public String createNewPost(@Valid @ModelAttribute Post post, BindingResult bindingResult, SessionStatus sessionStatus) {
        if (bindingResult.hasErrors()) {
            return "postForm";
        }
        postService.save(post);
        sessionStatus.setComplete();
        return "redirect:/post/" + post.getId();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/editPost/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            // Get current username from security context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String authUsername = auth != null ? auth.getName() : "anonymousUser";

            if (authUsername.equals(post.getUser().getUsername())) {
                model.addAttribute("post", post);
                return "postForm";
            } else {
                return "403"; // Ensure you have a proper 403 error page
            }
        } else {
            return "error"; // Ensure you have a proper error page
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/deletePost/{id}")
    public String deletePost(@PathVariable Long id) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            // Get current username from security context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String authUsername = auth != null ? auth.getName() : "anonymousUser";

            if (authUsername.equals(post.getUser().getUsername())) {
                postService.deleteById(post.getId());
                return "redirect:/";
            } else {
                return "403"; // Ensure you have a proper 403 error page
            }
        } else {
            return "error"; // Ensure you have a proper error page
        }
    }
}
