package org.blogify.blogapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Principal principal) {
        if (principal != null) {
            // User is already logged in; redirect to home page or some other secure page
            return "redirect:/"; // or "redirect:/home" if you have a dedicated home page
        } else {
            // User is not logged in; show the login page
            return "login";
        }
    }

}
