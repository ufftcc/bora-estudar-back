package com.ufftcc.boraestudar.controllers;

import com.ufftcc.boraestudar.dtos.user.UserCreateDto;
import com.ufftcc.boraestudar.dtos.user.UserLoginDto;
import com.ufftcc.boraestudar.dtos.user.UserLoginResponseDto;
import com.ufftcc.boraestudar.dtos.user.UserResponseBasicDto;
import com.ufftcc.boraestudar.entities.User;
import com.ufftcc.boraestudar.mappers.UserMapper;
import com.ufftcc.boraestudar.services.JwtService;
import com.ufftcc.boraestudar.services.LoginService;
import com.ufftcc.boraestudar.services.UserService;
import jakarta.validation.Valid;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserMapper mapper;

    public LoginController(UserService userService, LoginService loginService, JwtService jwtService, UserMapper mapper) {
        this.userService = userService;
        this.loginService = loginService;
        this.jwtService = jwtService;
        this.mapper = mapper;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseBasicDto createAccount(@Valid @RequestBody UserCreateDto dto) {
        User registeredUser = userService.create(dto);
        return mapper.toTransferObject(registeredUser, UserResponseBasicDto.class);
    }

    @GetMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public String confirmAccount(@RequestParam String token) throws IOException {
        userService.confirmUser(token);
        ClassPathResource htmlFile = new ClassPathResource("static/confirm-success.html");
        return StreamUtils.copyToString(htmlFile.getInputStream(), StandardCharsets.UTF_8);
    }

    @PostMapping("/signing")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponseDto enterAccount(@RequestBody UserLoginDto dto) {
        User authenticatedUser = loginService.authenticate(dto);
        String jwtToken = jwtService.generateToken(authenticatedUser.getEmail());
        return new UserLoginResponseDto(jwtToken, jwtService.getExpirationTime());
    }
}
