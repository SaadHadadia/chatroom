package com.example.chatroom.repositories;

import com.example.chatroom.models.User.PersonalAccessToken;
import com.example.chatroom.models.User.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalAccessTokenRepository extends JpaRepository<PersonalAccessToken, Long> {
    Optional<PersonalAccessToken> findByToken(String token);
    List<PersonalAccessToken> findAllByUserId(Long userId);
    List<PersonalAccessToken> findAllByType(TokenType type);

    List<PersonalAccessToken> findAllByUserIdAndType(Long userId, TokenType type);
}
