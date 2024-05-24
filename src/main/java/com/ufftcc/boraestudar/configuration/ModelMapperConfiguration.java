package com.ufftcc.boraestudar.configuration;

import com.ufftcc.boraestudar.configuration.mapper_converter.WeekdayToStudyGroupWeekday;
import com.ufftcc.boraestudar.configuration.mapper_converter.StudyGroupUserToUserResponseBasicDto;
import com.ufftcc.boraestudar.configuration.mapper_converter.StudyGroupWeekdayToWeekday;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public final class ModelMapperConfiguration {

    public static final String MODEL_MAPPER_BEAN_NAME = "modelMapper";

    @Bean(name = MODEL_MAPPER_BEAN_NAME)
    ModelMapper createModelMapperBean() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setCollectionsMergeEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        modelMapper.addConverter(new StudyGroupUserToUserResponseBasicDto());
        modelMapper.addConverter(new StudyGroupWeekdayToWeekday());
        modelMapper.addConverter(new WeekdayToStudyGroupWeekday());

        return modelMapper;
    }
}
