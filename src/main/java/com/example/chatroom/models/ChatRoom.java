package com.example.chatroom.models;

import com.example.chatroom.models.User.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a specialized chat room with an assigned tutor.
 * Extends the base Chat class.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class ChatRoom extends Chat {

    @Column(name = "name", nullable = false)
    String name;

    @ManyToMany
    @JoinTable(
            name = "chatroom_admin",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id")
    )
    private Set<User> admins = new HashSet<>();

    @Transient
    private Message latestMessage;

}
