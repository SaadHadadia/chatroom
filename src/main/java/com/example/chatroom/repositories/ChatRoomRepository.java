package com.example.chatroom.repositories;

import com.example.chatroom.models.ChatRoom;
import com.example.chatroom.models.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    
    // Find chat rooms where a user is a participant
    List<ChatRoom> findByUsersContaining(User user);
    
    // Find chat rooms where a user is an admin
    List<ChatRoom> findByAdminsContaining(User admin);
    
    // Find chat rooms where a user is either a participant or an admin
    @Query("SELECT DISTINCT cr FROM ChatRoom cr " +
           "WHERE :user MEMBER OF cr.users OR :user MEMBER OF cr.admins")
    List<ChatRoom> findByUserOrAdmin(@Param("user") User user);
    
    // Find a chat room by ID and check if user is a member or admin
    @Query("SELECT cr FROM ChatRoom cr " +
           "WHERE cr.id = :roomId AND (:user MEMBER OF cr.users OR :user MEMBER OF cr.admins)")
    Optional<ChatRoom> findByIdAndUserHasAccess(@Param("roomId") Long roomId, @Param("user") User user);
}
