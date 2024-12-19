package com.example.chatroom.repositories;

import com.example.chatroom.models.User.User;
import com.example.chatroom.models.User.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    Optional<User> findByEmailAndEmailVerifiedAtIsNotNull(String email);
    
    List<User> findByProfile(Profile profile);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.firstname LIKE %:searchTerm% OR u.lastname LIKE %:searchTerm% OR u.email LIKE %:searchTerm%")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT u FROM User u WHERE u.emailVerifiedAt IS NULL")
    List<User> findUnverifiedUsers();
    
    // @Query("SELECT u FROM User u WHERE u.lastLoginAt < :date")
    // List<User> findInactiveUsers(@Param("date") LocalDateTime date);
    
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.emailVerifiedAt IS NOT NULL")
    boolean isEmailVerified(@Param("email") String email);
}
