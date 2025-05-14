package com.ufftcc.boraestudar.dtos.studygroup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ufftcc.boraestudar.dtos.subject.SubjectResponseDto;
import com.ufftcc.boraestudar.dtos.user.UserResponseBasicDto;
import com.ufftcc.boraestudar.entities.Weekday;
import com.ufftcc.boraestudar.enums.ModalityEnum;

import java.time.LocalTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StudyGroupResponseDto {
    private Long id;

    private String title;

    private String description;

    private Long ownerId;

    private SubjectResponseDto subject;

    private List<UserResponseBasicDto> students;

    private Integer maxStudents;

    private LocalTime meetingTime;

    private List<Weekday> studyGroupWeekdays;

    private String discordInviteUrl;

    @JsonIgnore
    private Boolean isPrivate;

    private ModalityEnum modality;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public SubjectResponseDto getSubject() {
        return subject;
    }

    public void setSubject(SubjectResponseDto subject) {
        this.subject = subject;
    }

    public List<UserResponseBasicDto> getStudents() {
        return students;
    }

    public void setStudents(List<UserResponseBasicDto> students) {
        this.students = students;
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

    public List<Weekday> getWeekdays() {
        return studyGroupWeekdays;
    }

    public void setWeekdays(List<Weekday> studyGroupWeekdays) {
        this.studyGroupWeekdays = studyGroupWeekdays;
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

    public String getDiscordInviteUrl() {
        return discordInviteUrl;
    }

    public void setDiscordInviteUrl(String discordInviteUrl) {
        this.discordInviteUrl = discordInviteUrl;
    }
}
