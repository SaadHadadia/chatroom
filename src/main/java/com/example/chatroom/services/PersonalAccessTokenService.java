package com.example.chatroom.services;

import com.example.chatroom.models.User.PersonalAccessToken;
import com.example.chatroom.models.User.TokenType;
import com.example.chatroom.repositories.PersonalAccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PersonalAccessTokenService {
    @Autowired
    PersonalAccessTokenRepository tokenRepository;

    public PersonalAccessToken createToken(PersonalAccessToken token) {
        return tokenRepository.save(token);
    }

    public Optional<PersonalAccessToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public List<PersonalAccessToken> findAllByUserId(Long userId) {
        return tokenRepository.findAllByUserId(userId);
    }

    public List<PersonalAccessToken> findAllByType(TokenType type) {
        return tokenRepository.findAllByType(type);
    }

    public boolean isTokenValid(String token) {
        return findByToken(token)
                .map(PersonalAccessToken::isValid)
                .orElse(false);
    }

    public void deleteToken(PersonalAccessToken token) {
        tokenRepository.delete(token);
    }

    public void expireToken(PersonalAccessToken token) {
        token.setExpiresAt(LocalDateTime.now());  // Immediately expire the token
        tokenRepository.save(token);
    }

    public void updateToken(PersonalAccessToken dbToken) {
        // This method ensures the token is updated in the database
        // It's useful for marking tokens as used, updating expiration, etc.
        tokenRepository.save(dbToken);
    }

    // Optional: Method to invalidate all tokens of a specific type for a user
    public void invalidateTokens(Long userId, TokenType type) {
        List<PersonalAccessToken> tokens = tokenRepository.findAllByUserIdAndType(userId, type);
        tokens.forEach(this::expireToken);
    }
}