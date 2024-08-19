package com.ufftcc.boraestudar.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscordUserResponseDto {

    private String id;

    private String username;

    private String avatar;

    private String global_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGlobal_name() {
        return global_name;
    }

    public void setGlobal_name(String global_name) {
        this.global_name = global_name;
    }
}