package com.example.chatroom.controllers;

import com.example.chatroom.config.security.encryption.Encryptor;
import com.example.chatroom.models.Message;
import com.example.chatroom.models.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final Encryptor encryptor;

    @Autowired
    public ChatController(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(
            @Payload Message message
    ) {
        try {
            return encryptor.encrypt(message);
        }catch (Exception e){
            message.setType(MessageType.ERROR);
            message.setContent(e.toString());
            return message;
        }
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(
            @Payload Message message,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }
}