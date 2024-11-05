package com.example.chatroom.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a user with attributes like name, email, keys, and chats.
 */
@Data
@Entity
@Table(name = "user")
public class User implements UserDetails {

    // Primary key with auto-increment strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    protected String firstname;
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
    protected String email_verified_at = null;

    @Column(name = "created_at")
    protected String created_at;

    @Column(name = "updated_at")
    protected String updated_at;

    protected String privatekey;

    @Column(name = "publickey")
    protected String publickey;

    // Many-to-Many relationship between User and Chat entities
    @ManyToMany
    @JoinTable(
            name = "user_chat",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    protected Set<Chat> chats = new HashSet<>();

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

}
