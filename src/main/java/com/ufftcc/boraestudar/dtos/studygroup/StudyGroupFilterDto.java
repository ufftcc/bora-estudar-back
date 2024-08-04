package com.ufftcc.boraestudar.dtos.studygroup;

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
                                  ModalityEnum modality,
                                  Long ownerId) {

    public StudyGroupFilterDto(String title, String description, String subjectName, LocalTime meetingTime, List<Long> weekdays, ModalityEnum modality, Long ownerId) {
        this.title = title;
        this.description = description;
        this.subjectName = subjectName;
        this.meetingTime = meetingTime;
        this.weekdays = weekdays;
        this.modality = modality;
        this.ownerId= ownerId;
    }
}
