package com.ufftcc.boraestudar.dtos.studygroup;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ufftcc.boraestudar.entities.StudyGroupWeekday;
import com.ufftcc.boraestudar.entities.Weekday;
import com.ufftcc.boraestudar.enums.ModalityEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

public class StudyGroupUpdateDto {

    @NotNull
    @NotBlank
    private Long userId;

    private String title;

    private String description;

    private LocalTime meetingTime;

    @JsonIgnore
    private Boolean isPrivate;

    @JsonIgnore
    private ModalityEnum modality;

    @JsonAlias("weekdays")
    private List<Weekday> studyGroupWeekdays;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(LocalTime meetingTime) {
        this.meetingTime = meetingTime;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public ModalityEnum getModality() {
        return modality;
    }

    public void setModality(ModalityEnum modality) {
        this.modality = modality;
    }

    public List<Weekday> getStudyGroupWeekdays() {
        return studyGroupWeekdays;
    }

    public void setStudyGroupWeekdays(List<Weekday> studyGroupWeekdays) {
        this.studyGroupWeekdays = studyGroupWeekdays;
    }
}
