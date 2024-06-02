package com.ufftcc.boraestudar.mappers.converters;

import com.ufftcc.boraestudar.dtos.user.UserResponseBasicDto;
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
