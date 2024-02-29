package com.ufftcc.boraestudar.dto.mapper;

import com.ufftcc.boraestudar.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class UserMapper extends BaseEntityMapper<User> {

    public UserMapper(ModelMapper modelMapper) {
        super(User.class, modelMapper);
    }

}
