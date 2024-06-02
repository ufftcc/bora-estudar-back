package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.entities.EmailVerificationToken;
import com.ufftcc.boraestudar.entities.User;
import com.ufftcc.boraestudar.repositories.EmailVerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationTokenService {

    private final EmailVerificationTokenRepository tokenRepository;

    public EmailVerificationTokenService(EmailVerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
    public EmailVerificationToken createVerificationToken(User user) {
        EmailVerificationToken token = new EmailVerificationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setValidity(LocalDateTime.now().plusDays(1));
        return tokenRepository.save(token);
    }
}
