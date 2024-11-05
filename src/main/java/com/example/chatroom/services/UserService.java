package com.example.chatroom.services;

import com.example.chatroom.models.Profile;
import com.example.chatroom.models.User;
import com.example.chatroom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(User user, Profile role) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProfile(role); // Set the user's profile based on the Profile enum
        return userRepository.save(user);
    }
}