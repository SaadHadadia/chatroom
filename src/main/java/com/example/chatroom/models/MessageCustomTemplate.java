package com.example.chatroom.models;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MessageCustomTemplate {
    private String content;
    private String sender;
    private MessageType type;
    private LocalDateTime timestamp;
} 