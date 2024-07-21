package com.ufftcc.boraestudar.controllers;

import com.ufftcc.boraestudar.mappers.UserMapper;
import com.ufftcc.boraestudar.dtos.user.UserResponseBasicDto;
import com.ufftcc.boraestudar.dtos.user.UserUpdateDto;
import com.ufftcc.boraestudar.entities.User;
import com.ufftcc.boraestudar.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    public UserController(UserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseBasicDto> findAll() {
        List<User> users = service.findAll();
        return mapper.toTransferObjectList(users, UserResponseBasicDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseBasicDto findById(@PathVariable Long id) {
        User user = service.findById(id);
        return mapper.toTransferObject(user, UserResponseBasicDto.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseBasicDto updateById(@PathVariable Long id, @RequestBody UserUpdateDto dto) {
        User updatedUser = service.updateById(id, dto);
        return mapper.toTransferObject(updatedUser, UserResponseBasicDto.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}
