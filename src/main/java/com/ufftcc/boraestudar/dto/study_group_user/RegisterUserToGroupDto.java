package com.ufftcc.boraestudar.dto.study_group_user;

import jakarta.validation.constraints.NotNull;

public class RegisterUserToGroupDto {

    @NotNull(message = "userId param is required")
    private Long userId;

    @NotNull(message = "isTutor param is required")
    private Boolean isTutor;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getIsTutor() {
        return isTutor;
    }

    public void setIsTutor(Boolean isTutor) {
        this.isTutor = isTutor;
    }

}
