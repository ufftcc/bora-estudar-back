package com.ufftcc.boraestudar.dtos.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.common.util.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseBasicDto {

    private Long id;

    private String name;

    private String email;

    private String discordId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }

    @JsonProperty
    public boolean getIsDiscordAssociate() {

        return StringUtils.isNotBlank(getDiscordId());
    }
}
