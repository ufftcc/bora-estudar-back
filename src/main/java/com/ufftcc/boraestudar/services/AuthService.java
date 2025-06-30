package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.dtos.user.UserCreateDto;
import com.ufftcc.boraestudar.dtos.user.UserLoginDto;
import com.ufftcc.boraestudar.entities.EmailVerificationToken;
import com.ufftcc.boraestudar.entities.User;
import com.ufftcc.boraestudar.mappers.UserMapper;
import com.ufftcc.boraestudar.repositories.UserRepository;
import com.ufftcc.boraestudar.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailVerificationTokenService emailTokenService;

    @Value("${external.host}")
    private String host;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager,
                       UserMapper userMapper, PasswordEncoder passwordEncoder, EmailService emailService,
                       EmailVerificationTokenService emailTokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.emailTokenService = emailTokenService;
    }

    public User authenticate(UserLoginDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(),
                dto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return userRepository
                .findByEmail(dto.getEmail())
                .orElseThrow();
    }

    public User registerUser(UserCreateDto dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsEnabled(false);

        User registredUser = userRepository.save(user);
        String emailToken = emailTokenService.createVerificationToken(registredUser).getToken();
        // Descomentar para o email ser enviado de verdade
       emailService.sendEmail(registredUser.getEmail(), "Confirm your email",
               "Click on this link to confirm your email: "+host+"/confirm?token=" + emailToken);

        // System.out.println("Click on this link to confirm your email: http://localhost:4200/confirm?token=" + emailToken);

        return registredUser;
    }

    public String confirmUser(String token) {
        EmailVerificationToken verificationToken =
                emailTokenService.findByToken(token);

        if (verificationToken.getValidity().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        User user = verificationToken.getUser();
        user.setIsEnabled(true);
        userRepository.save(user);
        return "Email confirmed successfully";
    }
}
