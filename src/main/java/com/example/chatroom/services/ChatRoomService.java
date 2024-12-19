package com.example.chatroom.services;

import com.example.chatroom.models.ChatRoom;
import com.example.chatroom.models.User.User;
import com.example.chatroom.repositories.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomService {
    
    private final ChatRoomRepository chatRoomRepository;
    
    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }
    
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        if (chatRoom.getName() == null || chatRoom.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Chat room name cannot be empty");
        }

        // Ensure the sets are initialized
        if (chatRoom.getUsers() == null) {
            chatRoom.setUsers(new HashSet<>());
        }
        if (chatRoom.getAdmins() == null) {
            chatRoom.setAdmins(new HashSet<>());
        }
        if (chatRoom.getMessages() == null) {
            chatRoom.setMessages(new ArrayList<>());
        }

        return chatRoomRepository.save(chatRoom);
    }
    
    public List<ChatRoom> getChatRoomsByUser(User user) {
        return chatRoomRepository.findByUsersContaining(user);
    }
    
    public List<ChatRoom> getChatRoomsByAdmin(User admin) {
        return chatRoomRepository.findByAdminsContaining(admin);
    }
    
    public List<ChatRoom> getAllAccessibleChatRooms(User user) {
        return chatRoomRepository.findByUserOrAdmin(user);
    }
    
    public Optional<ChatRoom> getChatRoomWithAccess(Long roomId, User user) {
        return chatRoomRepository.findByIdAndUserHasAccess(roomId, user);
    }
    
    public void deleteChatRoom(Long roomId) {
        chatRoomRepository.deleteById(roomId);
    }
    
    public Optional<ChatRoom> getChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId);
    }
    
    public boolean isUserAdmin(ChatRoom chatRoom, User user) {
        return chatRoom.getAdmins().contains(user);
    }
    
    public boolean isUserMember(ChatRoom chatRoom, User user) {
        return chatRoom.getUsers().contains(user);
    }
    
    public Optional<ChatRoom> findById(Long id) {
        return chatRoomRepository.findById(id);
    }
    
    public List<ChatRoom> getAllChatRoomsForUser(User user) {
        return chatRoomRepository.findByUsersContaining(user);
    }
}
