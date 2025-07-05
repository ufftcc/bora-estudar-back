package com.ufftcc.boraestudar.controllers;

import com.ufftcc.boraestudar.dtos.user.UserResponseBasicDto;
import com.ufftcc.boraestudar.entities.User;
import com.ufftcc.boraestudar.mappers.BaseEntityMapper;
import com.ufftcc.boraestudar.mappers.UserMapper;
import com.ufftcc.boraestudar.services.AuthDiscordService;
import com.ufftcc.boraestudar.services.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discord")
public class AuthDiscordController {

    private final AuthDiscordService authDiscordService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Value("${external.host}")
    private String hostHomePage;

    public AuthDiscordController(AuthDiscordService authDiscordService, UserService userService, UserMapper userMapper) {
        this.authDiscordService = authDiscordService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseBasicDto doAuthDiscord(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam String code,
                                      @RequestParam String state) {

        String responseBody = authDiscordService.getDiscordUser(code,state);
        User user = userService.findById(Long.valueOf(state));
        return userMapper.toTransferObject(user, UserResponseBasicDto.class);
    }
}
