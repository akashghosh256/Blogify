package org.blogify.blogapp.controller;

import org.blogify.blogapp.model.BlogUser;
import org.blogify.blogapp.service.BlogUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionStatus;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SignupController {

    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    private final BlogUserService blogUserService;

    @Autowired
    public SignupController(BlogUserService blogUserService) {
        this.blogUserService = blogUserService;
    }

    @GetMapping("/signup")
    public String getRegisterForm(Model model) {
        model.addAttribute("blogUser", new BlogUser());
        return "registerForm";
    }

    @PostMapping("/register")
    public String registerNewUser(@Valid @ModelAttribute BlogUser blogUser, BindingResult bindingResult, SessionStatus sessionStatus) {
        logger.debug("Registering new user: {}", blogUser);

        if (blogUserService.findByUsername(blogUser.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "error.username", "Username is already registered. Try another one.");
            logger.warn("Username already taken: {}", blogUser.getUsername());
        }

        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors occurred: {}", bindingResult.getAllErrors());
            return "registerForm";
        }

        try {
            blogUserService.saveNewBlogUser(blogUser);
            // Create Authentication token and login after registering new blog user
            Authentication auth = new UsernamePasswordAuthenticationToken(blogUser, null, blogUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            logger.info("User registered and authenticated: {}", blogUser.getUsername());
            sessionStatus.setComplete();
            return "redirect:/";
        } catch (Exception e) {
            logger.error("Error during user registration: {}", e.getMessage());
            bindingResult.reject("registrationError", "An error occurred during registration. Please try again.");
            return "registerForm";
        }
    }
}
