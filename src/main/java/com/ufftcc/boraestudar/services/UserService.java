package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.mappers.UserMapper;
import com.ufftcc.boraestudar.dtos.user.UserCreateDto;
import com.ufftcc.boraestudar.dtos.user.UserUpdateDto;
import com.ufftcc.boraestudar.entities.EmailVerificationToken;
import com.ufftcc.boraestudar.entities.User;
import com.ufftcc.boraestudar.exceptions.user.UserNotFoundException;
import com.ufftcc.boraestudar.repositories.EmailVerificationTokenRepository;
import com.ufftcc.boraestudar.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final EmailService emailService;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final EmailVerificationTokenRepository tokenRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final UserMapper mapper;
    private final UserRepository repository;

    public UserService(EmailService emailService, EmailVerificationTokenService emailVerificationTokenService,
                       UserRepository repository, EmailVerificationTokenRepository tokenRepository,
                       PasswordEncoder bCryptPasswordEncoder, UserMapper mapper) {
        this.emailService = emailService;
        this.emailVerificationTokenService = emailVerificationTokenService;
        this.repository = repository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mapper = mapper;
    }

    public User create(UserCreateDto dto) {
        User user = mapper.toEntity(dto);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setIsEnabled(false);

        User registeredUser = repository.save(user);
        String token = emailVerificationTokenService.createVerificationToken(registeredUser).getToken();
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
            tokenRepository
                .findByToken(token)
                // TODO: Use a custom exception to redirect to fail page
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (verificationToken.getValidity().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        User user = verificationToken.getUser();
        user.setIsEnabled(true);
        repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
            user.getPassword(),
            user.isEnabled(),
            true,
            true,
            true,
            user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toSet()));
    }
}
