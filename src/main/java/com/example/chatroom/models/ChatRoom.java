package com.example.chatroom.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a specialized chat room with an assigned tutor.
 * Extends the base Chat class.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class ChatRoom extends Chat {

    // Many-to-One relationship with User to assign a tutor to the chat room
    @ManyToOne
    @JoinColumn(name = "tutor_id")  // Foreign key column for the tutor user
    private User tutor;

}
