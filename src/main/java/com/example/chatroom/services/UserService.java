package com.example.chatroom.services;

import com.example.chatroom.models.User.Profile;
import com.example.chatroom.models.User.User;
import com.example.chatroom.models.ChatRoom;
import com.example.chatroom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ChatRoomService chatRoomService;

    public User registerUser(User user, Profile role) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProfile(role); // Set the user's profile based on the Profile enum
        return userRepository.save(user);
    }

    public Optional<User> findById(long id){
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isEmailVerified(String email) {
        return userRepository.isEmailVerified(email);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findUnverifiedUsers() {
        return userRepository.findUnverifiedUsers();
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> searchUsers(String searchTerm) {
        return userRepository.searchUsers(searchTerm);
    }

    public List<User> findAllExceptCurrentUser(String currentUserEmail) {
        return userRepository.findAll().stream()
                .filter(user -> !user.getEmail().equals(currentUserEmail))
                .collect(Collectors.toList());
    }

    public List<User> findAllById(List<Long> userIds) {
        return userRepository.findAllById(userIds);
    }

    public List<User> findAllExceptChatRoomUsers(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        
        return userRepository.findAll().stream()
                .filter(user -> !chatRoom.getUsers().contains(user))
                .collect(Collectors.toList());
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public Optional<User> findByIdOptional(Long id) {
        return userRepository.findById(id);
    }

}