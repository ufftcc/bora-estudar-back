package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.dtos.user.UserLoginDto;
import com.ufftcc.boraestudar.entities.User;
import com.ufftcc.boraestudar.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public LoginService(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public User authenticate(UserLoginDto dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(),
            dto.getPassword()));

        return userRepository
            .findByEmail(dto.getEmail())
            .orElseThrow();
    }
}
