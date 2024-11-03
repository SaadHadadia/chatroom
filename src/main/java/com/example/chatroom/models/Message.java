package com.example.chatroom.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a message in a chat, including content, type, and sender information.
 */
@Builder
@Data
@NoArgsConstructor  // Required by JPA
@AllArgsConstructor // Required by @Builder
@Entity
@Table(name = "messages")
public class Message {

    // Primary key with auto-increment strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(name = "username", nullable = false)
    protected String username;

    // Enumerated type to store message type as a string
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    protected MessageType type;

    protected String content;
    protected String sender;
    protected String secretKey;

    // Many-to-One relationship with Chat, establishing foreign key in "messages" table
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

}
