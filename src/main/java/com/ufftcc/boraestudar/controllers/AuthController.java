package com.ufftcc.boraestudar.controllers;

import com.ufftcc.boraestudar.dtos.user.UserCreateDto;
import com.ufftcc.boraestudar.dtos.user.UserLoginDto;
import com.ufftcc.boraestudar.dtos.user.UserResponseBasicDto;
import com.ufftcc.boraestudar.entities.User;
import com.ufftcc.boraestudar.exceptions.JsonMessage;
import com.ufftcc.boraestudar.mappers.UserMapper;
import com.ufftcc.boraestudar.security.JwtService;
import com.ufftcc.boraestudar.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserMapper mapper;

    public AuthController(AuthService authService, JwtService jwtService, UserMapper mapper) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.mapper = mapper;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseBasicDto registerUser(@Valid @RequestBody UserCreateDto dto) {
        User registredUser = authService.registerUser(dto);
        return mapper.toTransferObject(registredUser, UserResponseBasicDto.class);
    }

    @GetMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public String confirmAccount(@RequestParam String token) {
        return authService.confirmUser(token);
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseBasicDto authenticateUser(@Valid @RequestBody UserLoginDto dto, HttpServletResponse response) {
        User authenticatedUser = authService.authenticate(dto);
        ResponseCookie jwtCookie = jwtService.generateJwtHttpOnlyCookie(authenticatedUser.getEmail());
        response.addHeader("Set-Cookie", jwtCookie.toString());
        return mapper.toTransferObject(authenticatedUser, UserResponseBasicDto.class);
    }

    @PostMapping("/signout")
    @ResponseStatus(HttpStatus.OK)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        ResponseCookie jwtCookie = jwtService.getCleanJwtCookie();
        response.addHeader("Set-Cookie", jwtCookie.toString());
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return new JsonMessage("Você foi desconectado!").toString();
    }
}
