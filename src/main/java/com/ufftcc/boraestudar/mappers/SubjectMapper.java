package com.ufftcc.boraestudar.mappers;

import com.ufftcc.boraestudar.entities.Subject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class SubjectMapper extends BaseEntityMapper<Subject> {

    public SubjectMapper(ModelMapper modelMapper) {
        super(Subject.class, modelMapper);
    }

}
