package com.example.chatroom.repositories;

import com.example.chatroom.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Find messages by chat ID
    List<Message> findByChatId(Long chatId);
    
    // Find messages by username
    List<Message> findByUsername(String username);
    
    // Find messages by chat ID and username
    List<Message> findByChatIdAndUsername(Long chatId, String username);
    
    List<Message> findByChatIdOrderByTimestampAsc(Long chatId);
    
    Message findTopByChatIdOrderByTimestampDesc(Long chatId);
}
