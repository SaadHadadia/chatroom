package com.example.chatroom.controllers;

import com.example.chatroom.config.UrlConfig;
import com.example.chatroom.models.User.PersonalAccessToken;
import com.example.chatroom.models.User.TokenType;
import com.example.chatroom.services.EmailService;
import com.example.chatroom.services.PersonalAccessTokenService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import com.example.chatroom.models.User.Profile;
import com.example.chatroom.models.User.User;
import com.example.chatroom.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService mailService;

    @Autowired
    private PersonalAccessTokenService tokenService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, 
                             @RequestParam("confirmPassword") String confirmPassword, 
                             Model model) {
        try {
            // Validate password
            String password = user.getPassword();
            if (!isPasswordValid(password)) {
                model.addAttribute("error", "Password must be at least 8 characters long and contain uppercase, lowercase, number, and special character");
                return "auth/register";
            }

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match");
                return "auth/register";
            }

            User createdUser = userService.registerUser(user, Profile.STUDENT);

            PersonalAccessToken token = createVerificationToken(createdUser);
            tokenService.createToken(token);

            mailService.sendVerificationEmail(
                    user.getEmail(),
                    user.getFirstname(),
                    UrlConfig.getUrl()+"/verify/" + token.getToken()
            );

            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Registration error", e);
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "auth/register";
        }
    }

    private boolean isPasswordValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        // Check for at least one uppercase letter
        boolean hasUppercase = Pattern.compile("[A-Z]").matcher(password).find();
        // Check for at least one lowercase letter
        boolean hasLowercase = Pattern.compile("[a-z]").matcher(password).find();
        // Check for at least one number
        boolean hasNumber = Pattern.compile("[0-9]").matcher(password).find();
        // Check for at least one special character
        boolean hasSpecial = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]").matcher(password).find();

        return hasUppercase && hasLowercase && hasNumber && hasSpecial;
    }

    private PersonalAccessToken createVerificationToken(User user) {
        PersonalAccessToken token = new PersonalAccessToken();
        token.setUser(user);
        token.setType(TokenType.VERIFY_EMAIL);
        token.setExpiresAt(LocalDateTime.now().plusDays(1));

        String uniqueToken = generateUniqueToken();
        token.setToken(uniqueToken);

        return token;
    }

    private String generateUniqueToken() {
        return UUID.randomUUID().toString();
    }

    @GetMapping("/verify/{token}")
    public String verifyToken(@PathVariable("token") String token, HttpSession session) {
        Optional<PersonalAccessToken> optionalDBToken = tokenService.findByToken(token);

        if (optionalDBToken.isEmpty()) {
            return "redirect:/error";
        }

        PersonalAccessToken dbToken = optionalDBToken.get();

        if (!isValidToken(dbToken)) {
            return "redirect:/error";
        }

        session.setAttribute("token", token);

        return determineRedirectPage(dbToken);
    }

    private boolean isValidToken(PersonalAccessToken token) {
        return token.getLastUsedAt() == null &&
                token.getExpiresAt().isAfter(LocalDateTime.now());
    }

    private String determineRedirectPage(PersonalAccessToken token) {
        if (token.getType() == null) {
            return "redirect:/error";
        }

        switch (token.getType()) {
            case RESET_PASSWORD:
                return "redirect:/password-reset";
            case VERIFY_EMAIL:
                return "redirect:/verify-email";
            default:
                return "redirect:/error";
        }
    }

    @GetMapping("/verify-email")
    public String verifyEmail(HttpSession session) {
        String token = (String) session.getAttribute("token");

        Optional<PersonalAccessToken> optionalDBToken = tokenService.findByToken(token);

        if (optionalDBToken.isEmpty()) {
            return "redirect:/error";
        }

        PersonalAccessToken dbToken = optionalDBToken.get();

        if (!isValidToken(dbToken)) {
            return "redirect:/error";
        }

        User user = dbToken.getUser();
        user.setEmailVerifiedAt(LocalDateTime.now());

        // Mark token as used
        dbToken.setLastUsedAt(LocalDateTime.now());

        userService.updateUser(user);
        tokenService.updateToken(dbToken);

        return "redirect:/email-verified-success";
    }

    @PostMapping("/password-reset")
    public String passwordReset(@RequestParam String token,
                                @RequestParam String newPassword,
                                Model model) {
        // Implement password reset logic
        return "redirect:/login";
    }
}