package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.mappers.UserMapper;
import com.ufftcc.boraestudar.dtos.user.UserCreateDto;
import com.ufftcc.boraestudar.dtos.user.UserUpdateDto;
import com.ufftcc.boraestudar.entities.EmailVerificationToken;
import com.ufftcc.boraestudar.entities.User;
import com.ufftcc.boraestudar.exceptions.user.UserNotFoundException;
import com.ufftcc.boraestudar.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final EmailService emailService;
    private final EmailVerificationTokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final UserRepository repository;

    public UserService(EmailService emailService, EmailVerificationTokenService tokenService,
                       UserRepository repository, UserMapper mapper, PasswordEncoder passwordEncoder) {
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(UserCreateDto dto) {
        User user = mapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsEnabled(false);

        User registeredUser = repository.save(user);
        String token = tokenService.createVerificationToken(registeredUser).getToken();
        emailService.sendEmail(registeredUser.getEmail(), "Confirm your email",
        "Click on this link to confirm your email: http://localhost:8080/confirm?token=" + token);

        return registeredUser;
    }

    public User findById(Long id) {
        Optional<User> userFound = repository.findById(id);
        if (userFound.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return userFound.get();
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User updateById(Long id, UserUpdateDto dto) {
        Optional<User> userFound = repository.findById(id);
        if (userFound.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        User user = userFound.get();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return repository.save(user);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void confirmUser(String token) {
        EmailVerificationToken verificationToken =
                tokenService
                .findByToken(token);

        if (verificationToken.getValidity().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        User user = verificationToken.getUser();
        user.setIsEnabled(true);
        repository.save(user);
    }
}
