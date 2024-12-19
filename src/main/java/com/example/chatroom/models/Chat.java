package com.example.chatroom.models;

import com.example.chatroom.models.User.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a general chat entity, which can be extended to specific types of chats.
 */
@Data
@NoArgsConstructor  // Adds a no-argument constructor needed by JPA
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type") // Column to distinguish between different chat types
@Table(name="chats")
public class Chat {

    // Primary key with auto-increment strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    // Many-to-Many relationship with User
    @ManyToMany
    @JoinTable(
            name = "user_chat",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    protected Set<User> users = new HashSet<>();

    // One-to-Many relationship with Message, establishing foreign key in "messages" table
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

}
