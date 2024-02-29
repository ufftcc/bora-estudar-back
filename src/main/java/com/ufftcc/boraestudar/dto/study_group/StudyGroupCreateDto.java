package com.ufftcc.boraestudar.dto.study_group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ufftcc.boraestudar.dto.subject.SubjectResponseDto;
import com.ufftcc.boraestudar.dto.user.UserResponseBasicDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StudyGroupCreateDto {

    @NotNull(message = "Title group is required")
    @NotBlank(message = "Title group is required")
    private String title;

    @NotNull(message = "Description group is required")
    @NotBlank(message = "Description group is required")
    private String description;

    @NotNull(message = "Owner id is required")
    private Long ownerId;

    private UserResponseBasicDto tutor;

    @NotNull(message = "Subject is required")
    private SubjectResponseDto subject;

    @NotNull(message = "Max students is required")
    private Integer maxStudents;

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

    @JsonIgnore
    public Boolean hasTutor() {
        return tutor != null;
    }
}
