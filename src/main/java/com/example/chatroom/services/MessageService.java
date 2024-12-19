package com.example.chatroom.services;

import com.example.chatroom.models.Message;
import com.example.chatroom.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    
    private final MessageRepository messageRepository;

    // Create a new message
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    // Get message by ID
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    // Get all messages for a specific chat
    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.findByChatId(chatId);
    }

    // Get all messages from a specific user
    public List<Message> getMessagesByUsername(String username) {
        return messageRepository.findByUsername(username);
    }

    // Get messages from a specific user in a specific chat
    public List<Message> getMessagesByChatIdAndUsername(Long chatId, String username) {
        return messageRepository.findByChatIdAndUsername(chatId, username);
    }

    // Delete a message
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    // Delete all messages in a chat
    public void deleteMessagesByChatId(Long chatId) {
        List<Message> messages = messageRepository.findByChatId(chatId);
        messageRepository.deleteAll(messages);
    }

    public List<Message> findByChatRoomId(Long chatRoomId) {
        return messageRepository.findByChatIdOrderByTimestampAsc(chatRoomId);
    }

    public Message findLatestByChatRoomId(Long chatRoomId) {
        return messageRepository.findTopByChatIdOrderByTimestampDesc(chatRoomId);
    }
}
