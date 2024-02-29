package com.ufftcc.boraestudar.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCreateDto {

    @NotBlank(message = "O nome do usuario nao pode ser vazio")
    private String name;

    @NotBlank(message = "O email do usuario nao pode ser vazio")
    private String email;

    @NotBlank(message = "A senha do usuario nao pode ser vazia")
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
