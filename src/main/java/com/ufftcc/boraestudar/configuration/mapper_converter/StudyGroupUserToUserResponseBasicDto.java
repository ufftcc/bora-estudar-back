package com.ufftcc.boraestudar.configuration.mapper_converter;

import com.ufftcc.boraestudar.dto.user.UserResponseBasicDto;
import com.ufftcc.boraestudar.entities.StudyGroupUser;
import org.modelmapper.AbstractConverter;

public class StudyGroupUserToUserResponseBasicDto extends AbstractConverter<StudyGroupUser, UserResponseBasicDto> {

    @Override
    protected UserResponseBasicDto convert(StudyGroupUser studyGroupUser) {
        UserResponseBasicDto userResponseBasicDto = new UserResponseBasicDto();
        userResponseBasicDto.setId(studyGroupUser.getUser().getId());
        userResponseBasicDto.setName(studyGroupUser.getUser().getName());
        userResponseBasicDto.setEmail(studyGroupUser.getUser().getEmail());

        return userResponseBasicDto;
    }
}
