package com.example.chatroom.config.security.authorisation;

import com.example.chatroom.models.User.User;
import com.example.chatroom.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

@Component
public class UserSecurity {

    private final UserService userService;

    public UserSecurity(UserService userService) {
        this.userService = userService;
    }

    public boolean isEmailVerified() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken || authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String email = authentication.getName();
        if (email == null) {
            return false;
        }

        User user = userService.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return false;
        }

        System.out.println("==> user: " + user.getEmail());
        return user.isEmailVerified();
    }

    public boolean hasRole(String... roles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken || authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String email = authentication.getName();
        if (email == null) {
            return false;
        }

        User user = userService.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return false;
        }

        for (String role : roles) {
            if (user.getProfile().name().equals(role)) {
                return true;
            }
        }
        return false;
    }
}