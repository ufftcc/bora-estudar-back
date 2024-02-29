package com.ufftcc.boraestudar.dto.mapper;

import com.ufftcc.boraestudar.entities.Subject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class SubjectMapper extends BaseEntityMapper<Subject> {

    public SubjectMapper(ModelMapper modelMapper) {
        super(Subject.class, modelMapper);
    }

}
