package com.example.chatroom.models.User;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "personal_access_tokens")
public class PersonalAccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Reference by user ID instead of email
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    protected TokenType type;

    @Column(name = "token", unique = true, nullable = false)
    protected String token;

    @Column(name = "last_used_at", nullable = true)
    protected LocalDateTime lastUsedAt;

    @Column(name = "expires_at", nullable = false)
    protected LocalDateTime expiresAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public boolean isValid() {
        if (expiresAt == null) return false;
        if (lastUsedAt != null) return false;
        return expiresAt.isAfter(LocalDateTime.now());
    }
}
