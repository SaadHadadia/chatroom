package com.example.chatroom.controllers;

import org.springframework.ui.Model;
import com.example.chatroom.models.Profile;
import com.example.chatroom.models.User;
import com.example.chatroom.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Add an empty user object to the model
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        userService.registerUser(user, Profile.STUDENT); // Register user with default role
        return "redirect:/login"; // Redirect to login page after successful registration
    }
}
