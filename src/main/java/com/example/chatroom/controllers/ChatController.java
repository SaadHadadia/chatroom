package com.example.chatroom.controllers;

import com.example.chatroom.models.MessageCustomTemplate;
import com.example.chatroom.models.ChatRoom;
import com.example.chatroom.models.Message;
import com.example.chatroom.models.MessageType;
import com.example.chatroom.models.User.User;
import com.example.chatroom.services.ChatRoomService;
import com.example.chatroom.services.MessageService;
import com.example.chatroom.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;


    @GetMapping("/chatroom/{chatRoomId}")
    public String chatRoom(@PathVariable Long chatRoomId, Model model) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = userService.findByEmail(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            
            // Get current chat room
            ChatRoom chatRoom = chatRoomService.getChatRoomWithAccess(chatRoomId, currentUser)
                    .orElseThrow(() -> new IllegalArgumentException("Chat Room not found or access denied"));

            // Get all chat rooms for the user with their latest messages
            List<ChatRoom> userChatRooms = chatRoomService.getAllChatRoomsForUser(currentUser);
            
            // For each chat room, get its latest message
            for (ChatRoom room : userChatRooms) {
                Message latestMessage = messageService.findLatestByChatRoomId(room.getId());
                room.setLatestMessage(latestMessage);
            }

            String fullName = currentUser.getFirstname() + " " + currentUser.getLastname();

            // Fetch messages for current chat room
            List<Message> messages = messageService.findByChatRoomId(chatRoomId);

            model.addAttribute("chatRoomId", chatRoomId);
            model.addAttribute("chatRoomName", chatRoom.getName());
            model.addAttribute("username", username);
            model.addAttribute("fullName", fullName);
            model.addAttribute("messages", messages);
            model.addAttribute("userChatRooms", userChatRooms);

            return "chat";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/error";
        }
    }

    // Add a custom error handler
    @GetMapping("/error")
    public String handleError(Model model) {
        // You can customize this error page as needed
        return "error";
    }


    @MessageMapping("/chatroom/{chatRoomId}/sendMessage")
    @SendTo("/topic/chatroom/{chatRoomId}")
    public MessageCustomTemplate sendMessage(
            @Payload MessageCustomTemplate messageTemplate,
            @DestinationVariable long chatRoomId
    ) {
        // Create a new Message entity from the template
        Message message = Message.builder()
                .content(messageTemplate.getContent())
                .sender(messageTemplate.getSender())
                .username(messageTemplate.getSender())
                .type(messageTemplate.getType())
                .timestamp(LocalDateTime.now())
                .chat(chatRoomService.findById(chatRoomId)
                        .orElseThrow(() -> new IllegalArgumentException("Chat room not found")))
                .build();

        // Save the message to the database
        messageService.saveMessage(message);

        return messageTemplate;
    }


    @MessageMapping("/chatroom/{chatRoomId}/addUser")
    @SendTo("/topic/chatroom/{chatRoomId}")
    public MessageCustomTemplate addUser(
            @Payload MessageCustomTemplate message,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", message.getSender());

        System.out.println("==>"+message.getSender());

        return message;
    }

    @GetMapping("/createChat")
    public String showCreateChatForm(Model model) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        // Get all users except the current user
        List<User> availableUsers = userService.findAllExceptCurrentUser(currentUsername);
        model.addAttribute("availableUsers", availableUsers);
        return "createChat";
    }

    @PostMapping("/createChat")
    public String createChatRoom(
        @RequestParam String name,
        @RequestParam(required = false) List<Long> userIds
    ) {
        try {
            // Get current user
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = userService.findByEmail(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Create new chat room
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setName(name);
            
            // Add current user as both admin and member
            chatRoom.getAdmins().add(currentUser);
            chatRoom.getUsers().add(currentUser);
            
            // Add selected users if any
            if (userIds != null && !userIds.isEmpty()) {
                List<User> selectedUsers = userService.findAllById(userIds);
                chatRoom.getUsers().addAll(selectedUsers);
            }
            
            // Save the chat room
            ChatRoom savedChatRoom = chatRoomService.createChatRoom(chatRoom);

            return "redirect:/chatroom/" + savedChatRoom.getId();
        } catch (Exception e) {
            return "redirect:/error";
        }
    }
}