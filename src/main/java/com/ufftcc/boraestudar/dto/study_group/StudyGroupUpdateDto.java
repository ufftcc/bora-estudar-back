package com.ufftcc.boraestudar.dto.study_group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ufftcc.boraestudar.enums.ModalityEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class StudyGroupUpdateDto {

    @NotNull
    @NotBlank
    private Long userId;

    private String title;

    private String description;

    @JsonIgnore
    private LocalTime meetingTime;

    @JsonIgnore
    private Boolean isPrivate;

    private ModalityEnum modality;

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
}
