package com.ufftcc.boraestudar.configuration;

import com.ufftcc.boraestudar.configuration.mapper_converter.StudyGroupUserToUserResponseBasicDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public final class ModelMapperConfiguration {

    public static final String MODEL_MAPPER_BEAN_NAME = "modelMapper";

    @Bean(name = MODEL_MAPPER_BEAN_NAME)
    ModelMapper createModelMapperBean() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setCollectionsMergeEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        return modelMapper;
    }
}
