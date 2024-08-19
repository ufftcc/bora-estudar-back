package com.ufftcc.boraestudar.controllers;

import com.ufftcc.boraestudar.services.AuthDiscordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discord")
public class AuthDiscordController {

    private final AuthDiscordService authDiscordService;

    public AuthDiscordController(AuthDiscordService authDiscordService) {
        this.authDiscordService = authDiscordService;
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String doAuthDiscord(HttpServletRequest request, HttpServletResponse response, @RequestParam String code, @PathVariable String id) {

        return authDiscordService.getDiscordUser(code,id);
    }
}
