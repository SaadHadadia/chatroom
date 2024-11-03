package com.example.chatroom.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user with attributes like name, email, keys, and chats.
 */
@Data
@Entity
@Table(name = "user")
public class User {

    // Primary key with auto-increment strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    protected String firstname;
    protected String lastname;

    @Column(name = "email", unique = true, nullable = false)
    protected String email;

    @Column(name = "password", nullable = false)
    protected String password;

    // Enumerated type to store profile as a string (e.g., ADMIN, TUTOR, STUDENT)
    @Enumerated(EnumType.STRING)
    @Column(name = "profile", nullable = false)
    protected Profile profile = Profile.STUDENT;

    @Column(name = "privatekey")
    protected String privatekey;

    @Column(name = "publickey")
    protected String publickey;

    // Many-to-Many relationship between User and Chat entities
    @ManyToMany
    @JoinTable(
            name = "user_chat",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    protected Set<Chat> chats = new HashSet<>();

}
