package org.blogify.blogapp.controller;

import org.blogify.blogapp.model.BlogUser;
import org.blogify.blogapp.model.Comment;
import org.blogify.blogapp.model.Post;
import org.blogify.blogapp.service.BlogUserService;
import org.blogify.blogapp.service.CommentService;
import org.blogify.blogapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@SessionAttributes("comment")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final PostService postService;
    private final BlogUserService blogUserService;
    private final CommentService commentService;

    @Autowired
    public CommentController(PostService postService, BlogUserService blogUserService, CommentService commentService) {
        this.postService = postService;
        this.blogUserService = blogUserService;
        this.commentService = commentService;
    }

    @Secured("ROLE_USER")
    @GetMapping("/comment/{id}")
    public String showComment(@PathVariable Long id, Model model, Principal principal) {

        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        Optional<BlogUser> optionalBlogUser = this.blogUserService.findByUsername(authUsername);
        Optional<Post> postOptional = this.postService.getById(id);

        if (postOptional.isPresent() && optionalBlogUser.isPresent()) {
            Comment comment = new Comment();
            comment.setPost(postOptional.get());
            comment.setUser(optionalBlogUser.get());
            model.addAttribute("comment", comment);
            logger.debug("GET comment/{id}: {} / {}", comment, id); // for testing debugging purposes
            return "commentForm";
        } else {
            logger.error("Could not find a post by id: {} or user by logged in username: {}", id, authUsername); // for testing debugging purposes
            return "error";
        }
    }

    @Secured("ROLE_USER")
    @PostMapping("/comment")
    public String validateComment(@Valid @ModelAttribute Comment comment, BindingResult bindingResult, SessionStatus sessionStatus) {
        logger.debug("POST comment: {}", comment); // for testing debugging purposes
        if (bindingResult.hasErrors()) {
            logger.error("Comment did not validate");
            return "commentForm";
        } else {
            this.commentService.save(comment);
            logger.debug("SAVE comment: {}", comment); // for testing debugging purposes
            sessionStatus.setComplete();
            return "redirect:/post/" + comment.getPost().getId();
        }
    }
}
