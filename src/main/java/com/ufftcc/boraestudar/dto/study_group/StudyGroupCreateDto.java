package com.ufftcc.boraestudar.dto.study_group;

import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ufftcc.boraestudar.dto.subject.SubjectResponseDto;
import com.ufftcc.boraestudar.dto.user.UserResponseBasicDto;

import com.ufftcc.boraestudar.entities.Weekday;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public class StudyGroupCreateDto {

    @NotNull(message = "Title group can't be null")
    @NotBlank(message = "Title can't be empty")
    private String title;

    @NotNull(message = "Description can't be null")
    @NotBlank(message = "Description can't be empty")
    private String description;

    @NotNull(message = "Owner id can't be null")
    private Long ownerId;

    private UserResponseBasicDto tutor;

    @NotNull(message = "Subject can't be null")
    private SubjectResponseDto subject;

    @NotNull(message = "Max students is required")
    private Integer maxStudents;

    @NotNull(message = "Meeting time is required")
    private LocalTime meetingTime;

    @JsonAlias("weekdays")
    @NotNull(message = "Weekdays is required")
    private
    List<Weekday> studyGroupWeekdays;

    private Boolean isPrivate;

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public UserResponseBasicDto getTutor() {
        return tutor;
    }

    public void setTutor(UserResponseBasicDto tutor) {
        this.tutor = tutor;
    }

    public SubjectResponseDto getSubject() {
        return subject;
    }

    public void setSubject(SubjectResponseDto subject) {
        this.subject = subject;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public LocalTime getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(LocalTime meetingTime) {
        this.meetingTime = meetingTime;
    }

    public List<Weekday> getStudyGroupWeekdays() {
        return studyGroupWeekdays;
    }

    public void setStudyGroupWeekdays(List<Weekday> studyGroupWeekdays) {
        this.studyGroupWeekdays = studyGroupWeekdays;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
    
    @JsonIgnore
    public Boolean hasTutor() {
        return tutor != null;
    }

}
