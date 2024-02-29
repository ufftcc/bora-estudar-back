package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.dto.mapper.UserMapper;
import com.ufftcc.boraestudar.dto.user.UserCreateDto;
import com.ufftcc.boraestudar.dto.user.UserUpdateDto;
import com.ufftcc.boraestudar.entities.User;
import com.ufftcc.boraestudar.exceptions.user.UserNotFoundException;
import com.ufftcc.boraestudar.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public User create(UserCreateDto dto) {
        User user = mapper.toEntity(dto);
        return repository.save(user);
    }

    public User findById(Long id) {
        Optional<User> userFound = repository.findById(id);
        if (userFound.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return userFound.get();
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User updateById(Long id, UserUpdateDto dto) {
        Optional<User> userFound = repository.findById(id);
        if (userFound.isEmpty()){
            throw new UserNotFoundException(id);
        }

        User user = userFound.get();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return repository.save(user);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
