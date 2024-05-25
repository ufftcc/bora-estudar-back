package com.ufftcc.boraestudar.dto.study_group;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ufftcc.boraestudar.enums.ModalityEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;

public record StudyGroupFilterDto(String title,
                                  String description,
                                  String subjectName,
                                  @DateTimeFormat(pattern = "HH:mm")
                                  LocalTime meetingTime,
                                  @JsonAlias("weekdays")
                                  List<Long> weekdays,
                                  ModalityEnum modality) {

    public StudyGroupFilterDto(String title, String description, String subjectName, LocalTime meetingTime, List<Long> weekdays, ModalityEnum modality) {
        this.title = title;
        this.description = description;
        this.subjectName = subjectName;
        this.meetingTime = meetingTime;
        this.weekdays = weekdays;
        this.modality = modality;
    }
}
