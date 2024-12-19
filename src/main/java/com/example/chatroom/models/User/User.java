package com.example.chatroom.models.User;

import com.example.chatroom.models.Chat;
import com.example.chatroom.models.ChatRoom;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a user with attributes like name, email, keys, and chats.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    // Primary key with auto-increment strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(name = "firstname", nullable = false)
    protected String firstname;

    @Column(name = "lastname", nullable = false)
    protected String lastname;

    @Column(name = "email", unique = true, nullable = false)
    protected String email;

    @Column(name = "password", nullable = false)
    protected String password;

    // Enumerated type to store profile as a string (e.g., ADMIN, TUTOR, STUDENT)
    @Enumerated(EnumType.STRING)
    @Column(name = "profile", nullable = false)
    protected Profile profile = Profile.STUDENT;

    @Column(name = "email_verified_at")
    protected LocalDateTime emailVerifiedAt = null;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    protected String privatekey;

    @Column(name = "publickey")
    protected String publickey;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonalAccessToken> tokens = new ArrayList<>();

    // Many-to-Many relationship between User and Chat entities
    @ManyToMany
    @JoinTable(
            name = "user_chat",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    protected Set<Chat> chats = new HashSet<>();

    @ManyToMany(mappedBy = "admins")
    protected Set<ChatRoom> adminChats = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + profile.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getRole(){
        return String.valueOf(profile);
    }

    public void setRole(Profile role) {
        this.profile = role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isEmailVerified() {
        return emailVerifiedAt != null;
    }

    // Consider adding a method to verify email
    public void verifyEmail() {
        this.emailVerifiedAt = LocalDateTime.now();
    }
}
