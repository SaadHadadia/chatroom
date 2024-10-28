package com.example.chatroom.controllers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessage {

    private String content;
    private String sender;
    private MessageType type;
    private String key;

    public String EncryptMessage(String content, String Key) {
        return this.content = content;
    }

    public String DecryptMessage(String content, String Key) {
        return this.content = content;
    }

    public String EncryptKey(String key, String PublicKey) {
        return this.key = content;
    }

    public String DecryptKey(String key, String PrivateKey) {
        return this.key = content;
    }
}
