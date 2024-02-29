package com.ufftcc.boraestudar.dto.study_group_user;

public class RegisterUserToGroupDto {

    private Long userId;

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
