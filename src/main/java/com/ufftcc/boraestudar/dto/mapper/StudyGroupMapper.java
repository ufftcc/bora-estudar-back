package com.ufftcc.boraestudar.dto.mapper;

import com.ufftcc.boraestudar.entities.StudyGroup;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public final class StudyGroupMapper extends BaseEntityMapper<StudyGroup> {

    public StudyGroupMapper(ModelMapper modelMapper) {
        super(StudyGroup.class, modelMapper);
    }

}
