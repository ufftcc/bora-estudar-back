package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.entities.EmailVerificationToken;
import com.ufftcc.boraestudar.entities.User;
import com.ufftcc.boraestudar.exceptions.security.TokenEmailNotFoundException;
import com.ufftcc.boraestudar.repositories.EmailVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationTokenService {

    @Value("${security.email.token.expiration-time-minutes}")
    int expirationTimeInMinutes;
    private final EmailVerificationTokenRepository tokenRepository;

    public EmailVerificationTokenService(EmailVerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
    public EmailVerificationToken createVerificationToken(User user) {
        EmailVerificationToken token = new EmailVerificationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setValidity(LocalDateTime.now().plusMinutes(expirationTimeInMinutes));
        return tokenRepository.save(token);
    }

    public EmailVerificationToken findByToken(String token) {
        return tokenRepository
                    .findByToken(token)
                    .orElseThrow(() -> new TokenEmailNotFoundException("Token not found"));
    }
}
