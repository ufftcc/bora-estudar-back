package com.ufftcc.boraestudar.controllers;

import com.ufftcc.boraestudar.services.AuthDiscordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discord")
public class AuthDiscordController {

    private final AuthDiscordService authDiscordService;

    @Value("${external.host}")
    private String hostHomePage;

    public AuthDiscordController(AuthDiscordService authDiscordService) {
        this.authDiscordService = authDiscordService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public void doAuthDiscord(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam String code,
                                      @RequestParam String state) {

        String responseBody = authDiscordService.getDiscordUser(code,state);

        try {
            response.sendRedirect(hostHomePage+"search?updated=true");
        } catch (Exception e) {
            throw new RuntimeException("Redirect failed", e);
        }
    }
}
